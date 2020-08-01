package cn.rectcircle.ms.server.thrift;

import org.apache.thrift.TException;

import cn.rectcircle.ms.thrift.hello.HelloService;

public class HelloServiceImpl implements HelloService.Iface {

    @Override
    public void ping() throws TException {
        System.out.println("ping");
    }

    @Override
    public int add(int num1, int num2) throws TException {
        return num1 + num2;
    }
    
}