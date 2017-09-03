package designmode.jdkProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by AXCUIAN on 2017/5/14.
 */
public class MyHandler implements InvocationHandler {
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始代理");
        method.invoke(args);
        System.out.println("结束代理");
        return null;
    }
}
