package cn.rectcircle.ms.client;

import cn.rectcircle.ms.discovery.ServiceDiscovery;

/**
 * Copyright (c) 2020, rectcircle. All rights reserved.
 * 
 * 微服务客户端工厂配置
 * 
 * @param <SC> SC 服务坐标类型
 * @author rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public class MsClientFactoryConfig<SC> {

    private final ServiceDiscovery<SC> discovery;

    private MsClientFactoryConfig(ServiceDiscovery<SC> discovery) {
        this.discovery = discovery;
    }

    public ServiceDiscovery<SC> getDiscovery() {
        return discovery;
    }

    public static <SC> Builder<SC> builder(ServiceDiscovery<SC> discovery) {
        return new Builder<>(discovery);
    }
    
    public static class Builder<SC> {
        private ServiceDiscovery<SC> discovery;

        public Builder(ServiceDiscovery<SC> discovery) {
            this.discovery = discovery;
        }

        public MsClientFactoryConfig<SC> build(){
            var result = new MsClientFactoryConfig<SC>(this.discovery);
            return result;
        }
    }
    
}
