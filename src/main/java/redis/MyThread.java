package redis;

import redis.clients.jedis.Jedis;

import java.awt.print.PrinterAbortException;

/**
 * Created by yxcui on 2017/4/26.
 */
public class MyThread implements Runnable {
    private Jedis jedis;
    public MyThread(Jedis jedis) {
        this.jedis = jedis;
    }

    public void run() {
        jedis.set("1","1");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
