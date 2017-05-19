package designmode.jdkproxy.mine.test01;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by yxcui on 2017/5/17.
 */
public class MyHandler implements InvocationHandler{

    private Object o;

    public MyHandler(Object o) {
        super();
        this.o = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("start");
        method.invoke(o);
        System.out.println("end");
        return null;
    }
}
