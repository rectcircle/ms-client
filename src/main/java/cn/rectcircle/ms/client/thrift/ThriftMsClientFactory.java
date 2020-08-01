package cn.rectcircle.ms.client.thrift;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import cn.rectcircle.ms.client.AbstractMsClientFactory;
import cn.rectcircle.ms.client.MsClientException;
import cn.rectcircle.ms.client.MsClientFactoryConfig;
import cn.rectcircle.ms.client.MsClientNetworkException;
import cn.rectcircle.ms.discovery.ServiceNode;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public class ThriftMsClientFactory<SC> extends AbstractMsClientFactory<SC, ThriftMsClientConfig<SC>> {

    private static final String CLIENT_SUFFIX = "$Client";
    private static final String IFACE_SUFFIX = "$Iface";

    public ThriftMsClientFactory(MsClientFactoryConfig<SC> factoryConfig) {
        super(factoryConfig);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> T newOriginClient(Class<T> clientClazz, ThriftMsClientConfig<SC> clientConfig,
            ServiceNode serviceNode) throws MsClientNetworkException, MsClientException {
        // get client class name
        String simpleName = clientClazz.getName();
        String name = simpleName;
        if (simpleName.endsWith(IFACE_SUFFIX)) {
            name = simpleName.substring(0, simpleName.indexOf(IFACE_SUFFIX));
        }
        name = name + CLIENT_SUFFIX;
        // verify client class exist
        Class<?> clientClass;
        try {
            clientClass = Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        // create client
        TTransport transport;
        transport = new TSocket(serviceNode.getHost(), serviceNode.getPort());
        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            Constructor<?> cons = clientClass.getConstructor(TProtocol.class);
            T res = (T) cons.newInstance(protocol);
            return res;
        } catch (TTransportException e) {
            throw new MsClientNetworkException("网络异常", e);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
            throw new MsClientException("构造 Origin Client 异常", e);
        }
    }
    
}