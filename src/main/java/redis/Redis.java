package redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Date;

/**
 * Created by yxcui on 2017/4/25.
 */
public class Redis {
    @Test
    public void main() throws InterruptedException {

        JedisPool pool = JedisUtil.getInstance().getPool("192.168.86.85",6379,"123456");
        for(int i=1;i<7;i++){
            Jedis j = JedisUtil.getInstance().getJedis("192.168.86.85",6379,"123456");
            System.out.println(i + (j==null?" null":" active"));

            //new Thread(new MyThread(j)).start();

            j.set("time",new Date().toString());
            System.out.println(" "+pool.getNumActive());
            System.out.println(" " + pool.getNumWaiters());
            System.out.println(" " + pool.getNumIdle());
            j.close();
        }

        Jedis jedis = new Jedis("192.168.86.85",6379);
        jedis.auth("123456");
        jedis.set("aa","aa");
        System.out.println(jedis.get("aa"));
    }



}
