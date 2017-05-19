package designmode.jdkproxy.proxy.test;

import designmode.jdkproxy.proxy.InvocationHandler;
import designmode.jdkproxy.proxy.Proxy;
import designmode.jdkproxy.proxy.TimeHandler;

public class Client {
	public static void main(String[] args) throws Exception {
		UserMgr mgr = new UserMgrImpl();
		InvocationHandler h = new TransactionHandler(mgr);
		//TimeHandler h2 = new TimeHandler(h);
		UserMgr u = (UserMgr)Proxy.newProxyInstance(UserMgr.class,h);
		u.addUser();
	}
}
