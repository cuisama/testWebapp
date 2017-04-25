package redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by yxcui on 2017/4/25.
 */
public class Redis {
    @Test
    public void main(){
        Jedis jedis = new Jedis("192.168.86.85",6379);
        jedis.auth("123456");
        jedis.set("aa","aa");
        System.out.println(jedis.get("aa"));
    }
}
