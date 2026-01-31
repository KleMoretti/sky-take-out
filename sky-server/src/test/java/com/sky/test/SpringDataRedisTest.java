package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest
public class SpringDataRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
        redisTemplate.opsForValue().set("key","value");
        String key = (String)redisTemplate.opsForValue().get("key");
        System.out.println("key="+key);
        redisTemplate.opsForValue().set("key2","value2",3, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().getAndSet("lock","2");
    }

    @Test
    public void testHash(){
        //hset hget hgetall
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("100","name","tom");
        hashOperations.put("100","age","20");

        String name = (String) hashOperations.get("100", "name");
        System.out.println("name="+name);

        Set keys = hashOperations.keys("100");
        System.out.println("keys="+keys);

        List values = hashOperations.values("100");
        System.out.println("values="+values);

        hashOperations.delete("100","age");
    }
}
