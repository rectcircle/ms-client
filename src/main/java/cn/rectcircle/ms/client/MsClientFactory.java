package cn.rectcircle.ms.client;

/**
 * Copyright (c) 2020, rectcircle. All rights reserved.
 * 
 * 微服务客户端工厂接口
 * 
 * @param <C> C 客户端配置
 * @author rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public interface MsClientFactory<C> {

    /**
     * 获取到 微服务 客户端的代理对象，保证线程安全，且支持如下特性
     * <ul>
     * <li>服务发现</li>
     * <li>负载均衡</li>
     * <li>自动重试</li>
     * <li>调用日志</li>
     * <li>metric</li>
     * <li>动态路由</li>
     * </ul>
     * 
     * @param <T> T 客户端类
     * @param clientClazz
     * @param clientConfig
     * @return
     */
    <T> T getClient(Class<T> clientClazz, C clientConfig);

}