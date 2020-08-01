package cn.rectcircle.ms.discovery;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-31
 * @version 0.0.1
 */
public class SimpleServiceDiscovery implements ServiceDiscovery<String> {

    private ConcurrentMap<String, List<ServiceNode>> serviceMap;

    private SimpleServiceDiscovery(ConcurrentMap<String, List<ServiceNode>> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    public List<ServiceNode> discovery(String serviceCoordinate) {
        return serviceMap.get(serviceCoordinate);
    }

    public static Builder builder(){
        return new Builder();
    }
    
    public static class Builder {
        private ConcurrentMap<String, List<ServiceNode>> serviceMap;

        public Builder() {
            this.serviceMap = new ConcurrentHashMap<String, List<ServiceNode>>();
        }

        public SimpleServiceDiscovery build() {
            return new SimpleServiceDiscovery(serviceMap);
        }

        public Builder addService(String serviceCoordinate, String host, int port) {
            var l = this.serviceMap.computeIfAbsent(serviceCoordinate, k -> new CopyOnWriteArrayList<>());
            l.add(new ServiceNode(host, port));
            return this;
        }
    }

}