# 微服务客户端封装

通过动态代理，给原始 RPC 客户端提供如下功能

* 线程安全单例
* 服务发现
* （未实现）负载均衡
* （未实现）自动重试
* （未实现）拦截器
* （未实现）熔断器
* （未实现）日志
* （未实现）metric

例子

```java
package cn.rectcircle.ms.client.thrift;

import org.junit.Assert;

import org.apache.thrift.TException;
import org.junit.Test;

import cn.rectcircle.ms.client.MsClientFactoryConfig;
import cn.rectcircle.ms.discovery.SimpleServiceDiscovery;
import cn.rectcircle.ms.thrift.hello.HelloService;

public class ThriftMsClientFactoryTests {

    @Test
    public void test() throws TException {
        // 1. 创建相关 RPC 框架 的 ClientFactory，配置服务发现
        var clientFactory = new ThriftMsClientFactory<>(MsClientFactoryConfig
                .builder(SimpleServiceDiscovery.builder().addService("helloService", "127.0.0.1", 9090).build())
                .build());
        // 2. 调用 getClient 传递 ClientConfig 和 Client 类的类型，获取客户端
        HelloService.Iface client = clientFactory.getClient(HelloService.Iface.class,
                ThriftMsClientConfig.builder("helloService").build());
        // 3. 调用相关 RPC 方法即可
        client.ping();
        Assert.assertEquals("add", 2, client.add(1, 1));
    }
}
```
