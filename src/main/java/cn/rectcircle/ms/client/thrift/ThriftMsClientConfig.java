package cn.rectcircle.ms.client.thrift;

import cn.rectcircle.ms.client.MsClientConfig;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public class ThriftMsClientConfig<T> implements MsClientConfig<T> {

    private final T serviceCoordinate;

    private ThriftMsClientConfig(T serviceCoordinate) {
        this.serviceCoordinate = serviceCoordinate;
    }

    @Override
    public T getServiceCoordinate() {
        return serviceCoordinate;
    }

    public static <T> Builder<T> builder(T serviceCoordinate) {
        return new Builder<>(serviceCoordinate);
    }

    public static class Builder<T> {
        private final T serviceCoordinate;

        public Builder(T serviceCoordinate) {
            this.serviceCoordinate = serviceCoordinate;
        }
        
        public ThriftMsClientConfig<T> build() {
            return new ThriftMsClientConfig<>(serviceCoordinate);
        }
    }

}