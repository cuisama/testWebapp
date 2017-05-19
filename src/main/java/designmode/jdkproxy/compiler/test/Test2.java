package designmode.jdkproxy.compiler.test;

import java.lang.reflect.Method;

public class Test2 {
	public static void main(String[] args) {
		Method[] methods = designmode.jdkproxy.proxy.Moveable.class.getMethods();
		for(Method m : methods) {
			System.out.println(m.getName());
		}
	}
}
