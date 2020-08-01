package cn.rectcircle.ms.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-31
 * @version 0.0.1
 */
public class ProxyClientInvocationHandler<SC, C extends MsClientConfig<SC>, T> implements InvocationHandler {

    private AbstractMsClientFactory<SC, C> clientFactory;
    private C clientConfig;
    private AbstractMsClientFactory.InternalCacheKey<SC, T> internalCacheKey;

    public ProxyClientInvocationHandler(AbstractMsClientFactory<SC, C> clientFactory, Class<T> clientClazz,
            C clientConfig) {
        this.clientFactory = clientFactory;
        this.clientConfig = clientConfig;
        this.internalCacheKey = new AbstractMsClientFactory.InternalCacheKey<>(
                clientConfig.getServiceCoordinate(),
                clientClazz);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // TODO 实现重试策略
        T client = this.clientFactory.borrowOriginClient(this.internalCacheKey, this.clientConfig);
        Object result = method.invoke(client, args);
        this.clientFactory.returnOriginClient(client, this.internalCacheKey);
        return result;
    }
    
}