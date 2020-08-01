package cn.rectcircle.ms.server.thrift;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.rectcircle.ms.thrift.hello.HelloService;

public class HelloServer {

    private static final Logger LOG = LoggerFactory.getLogger(HelloService.class);

    // thrift -out src/test/java --gen java src/test/thrift/hello.thrift
    public static void main(String[] args) {
        HelloService.Processor<HelloServiceImpl> processor = new HelloService.Processor<>(new HelloServiceImpl());
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
            LOG.info("Starting the simple server...");
            server.serve();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
