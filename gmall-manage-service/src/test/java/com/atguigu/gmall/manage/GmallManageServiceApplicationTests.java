package com.atguigu.gmall.manage;

import com.atguigu.gmall.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {
    @Autowired
    RedisUtil redisUtil;
	@Test
	public void contextLoads() {
		try {
            Jedis jedis = redisUtil.getJedis();
            jedis.getSet("test","test_value");
            jedis.close();
		}catch (JedisConnectionException e) {
			e.printStackTrace();
		}

	}
    @Test
    public void test(){
        Jedis jedis = new Jedis("192.168.74.128",6379);
    System.out.println(jedis.hget("hash", "uname"));
        jedis.hset("hash", "pwd", "123");
    }

    /**
     * 使用连接池
     */
    @Test
    public void test1(){
        //1.设置连接池的配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        //设置池中最大连接数
        config.setMaxTotal(50);
        //设置空闲时池中保有的最大连接数
        config.setMaxIdle(10);
        //2.设置连接池对象
        JedisPool pool = new JedisPool(config,"192.168.74.128",6379,5000);
        //3.从池中获取连接对象
        Jedis jedis = pool.getResource();
        System.out.println(jedis.hget("hash", "uname"));
        //4.连接归还池中
        jedis.close();

    }

}
