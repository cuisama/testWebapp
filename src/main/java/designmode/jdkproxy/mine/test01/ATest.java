package designmode.jdkproxy.mine.test01;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Created by yxcui on 2017/5/18.
 */
public class ATest {

    @Test
    public void main(){
        Machine um = new Tank();
        InvocationHandler h = new MyHandler(um);
        Machine u = (Machine) Proxy.newProxyInstance(Tank.class.getClassLoader(),Tank.class.getInterfaces(),h);
        u.move();
    }
}

