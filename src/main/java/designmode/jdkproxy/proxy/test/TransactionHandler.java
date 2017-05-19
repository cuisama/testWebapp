package designmode.jdkproxy.proxy.test;

import java.lang.reflect.Method;

import designmode.jdkproxy.proxy.InvocationHandler;

public class TransactionHandler implements InvocationHandler {
	
	private Object target;
	
	public TransactionHandler(Object target) {
		super();
		this.target = target;
	}

	@Override
	public void invoke(Object o, Method m) {
		System.out.println("Transaction Start");
		try {
			m.invoke(target);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Transaction Commit");
	}

}
