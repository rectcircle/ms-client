package cn.rectcircle.ms.discovery;

/**
 * Copyright (c) 2020, Rectcircle. All rights reserved.
 * 
 * @author Rectcircle
 * @date 2020-07-25
 * @version 0.0.1
 */
public class ServiceNode {
    private final String host;
    private final int port;

    public ServiceNode(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
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
        ServiceNode other = (ServiceNode) obj;
        if (host == null) {
            if (other.host != null) {
                return false;
            }
        } else if (!host.equals(other.host)) {
            return false;
        }
        if (port != other.port) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ServiceNode [host=" + host + ", port=" + port + "]";
    }
}