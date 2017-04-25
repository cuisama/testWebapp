package redis;

//import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import redis.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil
{
    //private Logger logger = Logger.getLogger(this.getClass().getName());

    private JedisUtil(){}

    private static class RedisUtilHolder{
        private static final JedisUtil instance = new JedisUtil();
    }

    public static JedisUtil getInstance(){
        return RedisUtilHolder.instance;
    }

    private static Map<String,JedisPool> maps = new HashMap<String,JedisPool>();

    private static JedisPool getPool(String ip, int port,String pwd){
        String key = ip+":"+port;
        JedisPool pool = null;
        if(!maps.containsKey(key))
        {
            JedisPoolConfig config = new JedisPoolConfig();
            //config.setMaxActive(RedisConfig.MAX_ACTIVE);
            config.setMaxIdle(RedisConfig.MAX_IDLE);
            //config.setMaxWait(RedisConfig.MAX_WAIT);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);

            if(pwd!=null){
                pool = new JedisPool(config,ip,port,RedisConfig.TIMEOUT,pwd);
            }else{
                pool = new JedisPool(config,ip,port,RedisConfig.TIMEOUT);
            }
            maps.put(key, pool);
        }
        else
        {
            pool = maps.get(key);
        }
        return pool;
    }

    public Jedis getJedis(String ip, int port,String pwd)
    {
        Jedis jedis = null;
        int count = 0;
        do
        {
            try
            {
                jedis = getPool(ip,port,pwd).getResource();
            }
            catch (Exception e)
            {
                //logger.error("get redis master1 failed!",e);
                getPool(ip,port,pwd).returnBrokenResource(jedis);
            }
        }
        while(jedis == null && count<RedisConfig.RETRY_NUM);
        return jedis;
    }

    public void closeJedis(Jedis jedis, String ip, int port,String pwd){
        if(jedis != null)
        {
            getPool(ip,port,pwd).returnResource(jedis);
        }
    }
}