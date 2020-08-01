package cn.rectcircle.ms.client;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

import cn.rectcircle.ms.discovery.ServiceNode;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @param <SC> SC 服务的坐标位置描述类
 * @param <C>  C 客户端的配置类型
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public abstract class AbstractMsClientFactory<SC, C extends MsClientConfig<SC>> implements MsClientFactory<C> {

    /** 工厂配置 */
    protected MsClientFactoryConfig<SC> factoryConfig;
    /** 服务节点数目 */
    protected ConcurrentMap<InternalCacheKey<SC, ?>, Integer> serviceNodeSizes;
    /** 服务节点 缓存 */
    protected ConcurrentMap<InternalCacheKey<SC, ?>, BlockingQueue<ServiceNode>> serviceNodes;
    /** 原始 Client 缓存 */
    protected ConcurrentMap<InternalCacheKey<SC, ?>, BlockingQueue<Object>> originClientCache;
    /** 代理 Client 缓存 */
    protected ConcurrentMap<InternalCacheKey<SC, ?>, Object> proxyClientCache;

    public static class InternalCacheKey<SC, T> {
        private final SC serviceCoordinate;
        private final Class<T> clientClazz;

        public InternalCacheKey(SC serviceCoordinate, Class<T> clientClazz) {
            this.serviceCoordinate = serviceCoordinate;
            this.clientClazz = clientClazz;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((clientClazz == null) ? 0 : clientClazz.hashCode());
            result = prime * result + ((serviceCoordinate == null) ? 0 : serviceCoordinate.hashCode());
            return result;
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            InternalCacheKey<SC, ?> other = (InternalCacheKey<SC, ?>) obj;
            if (clientClazz == null) {
                if (other.clientClazz != null) {
                    return false;
                }
            } else if (!clientClazz.equals(other.clientClazz)) {
                return false;
            }
            if (serviceCoordinate == null) {
                if (other.serviceCoordinate != null) {
                    return false;
                }
            } else if (!serviceCoordinate.equals(other.serviceCoordinate)) {
                return false;
            }
            return true;
        }

        public SC getServiceCoordinate() {
            return serviceCoordinate;
        }

        public Class<T> getClientClazz() {
            return clientClazz;
        }
    }

    public AbstractMsClientFactory(MsClientFactoryConfig<SC> factoryConfig) {
        this.factoryConfig = factoryConfig;
        this.serviceNodeSizes = new ConcurrentHashMap<>();
        this.originClientCache = new ConcurrentHashMap<>();
        this.proxyClientCache = new ConcurrentHashMap<>();
        this.serviceNodes = new ConcurrentHashMap<>();
    }

    /**
     * 各个 RPC 客户端自己实现，创建新的原始 Client
     * 
     * @param <T>          客户端类型
     * @param clientClazz  客户端类型类
     * @param clientConfig 客户端配置
     * @param serviceNode  客户端服务地址信息
     * @throws MsClientNetworkException 网络异常
     * @throws MsClientException 其他异常
     * @return 原始 Client 
     */
    protected abstract <T> T newOriginClient(Class<T> clientClazz, C clientConfig, ServiceNode serviceNode) throws MsClientNetworkException, MsClientException;

    /** 服务发现的同时记录服务节点数 */
    private BlockingQueue<ServiceNode> getServiceNodeBlockingQueue(InternalCacheKey<SC, ?> key) {
        BlockingQueue<ServiceNode> serviceNodes = this.serviceNodes.computeIfAbsent(key,
                psm -> new LinkedBlockingQueue<>());
        if (serviceNodes.size() == 0) {
            List<ServiceNode> tmp = this.factoryConfig.getDiscovery().discovery(key.getServiceCoordinate());
            if (tmp == null || tmp.size() == 0) {
                throw new MsClientException("未找到服务节点 Service Node Unfounded: " + key.getServiceCoordinate());
            }
            this.serviceNodeSizes.put(key, tmp.size());
            serviceNodes.addAll(tmp);
        }
        return serviceNodes;
    }

    @SuppressWarnings("unchecked")
    <T> T borrowOriginClient(InternalCacheKey<SC, T> key, C clientConfig) {
        BlockingQueue<Object> caches = originClientCache.computeIfAbsent(key, key1 -> new LinkedBlockingQueue<>());
        T result = (T) caches.poll();
        if (result == null) {
            // 获取 服务节点列表
            ServiceNode serviceNode = null;
            BlockingQueue<ServiceNode> serviceNodes = this.getServiceNodeBlockingQueue(key);
            serviceNode = serviceNodes.poll();
            result = this.newOriginClient(key.getClientClazz(), clientConfig, serviceNode);
        }
        return result;
    }

    <T> void returnOriginClient(T client, InternalCacheKey<SC, T> key) {
        BlockingQueue<Object> caches = this.originClientCache.computeIfAbsent(key, key1 -> new LinkedBlockingQueue<>());
        caches.offer(client);
    }

    <T> int getServiceNodeSize(InternalCacheKey<SC, T> key) {
        Integer result = this.serviceNodeSizes.get(key);
        if (result == null) {
            this.getServiceNodeBlockingQueue(key);
            result = this.serviceNodeSizes.get(key);
        }
        return result.intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClient(Class<T> clientClazz, C clientConfig) {
        return (T) this.proxyClientCache
                .computeIfAbsent(new InternalCacheKey<>(clientConfig.getServiceCoordinate(), clientClazz), config1 -> {
                    ProxyClientInvocationHandler<SC, C, T> proxy = new ProxyClientInvocationHandler<>(this, clientClazz,
                            clientConfig);
                    T clientProxy = (T) Proxy.newProxyInstance(clientClazz.getClassLoader(),
                            new Class<?>[] { clientClazz }, proxy);
                    return clientProxy;
                });
    }

}