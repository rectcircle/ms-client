namespace java cn.rectcircle.ms.thrift.hello

service HelloService {
    void ping(),
    i32 add(1:i32 num1, 2:i32 num2)
}