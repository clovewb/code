package com.santu.leaves.config.reids;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author LEAVES
 * @Date 2020/8/31
 * @Version 1.0
 */
@Configuration
public class JedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port ;
    @Bean
    public Jedis getJedis(){
       return  new Jedis("127.0.0.1",6379);
    }
//    封装jedispool对象（将配置对象注入其中）
    @Bean
    public JedisPool jedisPool(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig ){
        JedisPool pool = new JedisPool(jedisPoolConfig,host,port);
        return pool;
    }
//    封装jedispool配置对象
    @Bean("jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(1);
        poolConfig.setMaxTotal(10);
        return poolConfig;
    }
}
