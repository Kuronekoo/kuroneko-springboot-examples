package cn.kuroneko.demos.commons.utils;

import cn.kuroneko.demos.commons.properties.RedisProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.util.Pool;

import java.util.*;

/**
 * Created by A on 2018/1/29.
 */
public class JedisHelper {

    private final static Logger logger = LoggerFactory.getLogger(JedisHelper.class);
    /**
     * 只为代码不报错，RedisProperty需要手动写入或者从配置文件读取
     */
    private Pool<Jedis> jedisPool = redisPoolFactory(new RedisProperty());

    /**
     * 装配redispool
     * @param redisProperty
     * @return
     */
    public static  Pool<Jedis> redisPoolFactory(RedisProperty redisProperty) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(redisProperty.getMaxTotal());
        config.setMaxIdle(redisProperty.getMaxIdle());
        config.setMinIdle(redisProperty.getMinIdle());//设置最小空闲数
        config.setMaxWaitMillis(redisProperty.getMaxWaitMillis());
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        //Idle时进行连接扫描
        config.setTestWhileIdle(true);
        //表示idle object evitor两次扫描之间要sleep的毫秒数
        config.setTimeBetweenEvictionRunsMillis(30000);
        //表示idle object evitor每次扫描的最多的对象数
        config.setNumTestsPerEvictionRun(10);
        //表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
        config.setMinEvictableIdleTimeMillis(60000);

        Set<String> sentinelSet = new HashSet<String>(redisProperty.getSentinels());

        JedisSentinelPool redisPool =  new JedisSentinelPool(redisProperty.getMasterName(), sentinelSet, config);
        //单节点
//        JedisPool jedisPool = new JedisPool(config, host, port, timeout);

        logger.info("RedisPool init...................");

        return redisPool;
    }

    public void watch(String key,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.watch(key);
        Transaction transaction = jedis.multi();
        transaction.set(key,value);
        transaction.exec();
        transaction.close();
        jedis.close();
    }

    public void setEx(String key,int seconds,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.setex(key, seconds, value);
        jedis.close();
    }

    public String get(String key){
        Jedis jedis = jedisPool.getResource();
        String v =  jedis.get(key);
        jedis.close();
        return v;
    }

//    public String get(short db, String key) {
//        Jedis jedis = jedisPool.getResource();
//        jedis.select(db);
//        String v = jedis.get(key);
//        jedis.close();
//        return v;
//    }
//
//    public void set(short db, String key,String value) {
//        Jedis jedis = jedisPool.getResource();
//        jedis.select(db);
//        jedis.set(key, value);
//        jedis.close();
//    }

    public String set(String key,String value) {
        Jedis jedis = jedisPool.getResource();
        String retstr = jedis.set(key, value);
        jedis.close();
        return retstr;
    }

//    public String hget(short db, String key,String field) {
//        Jedis jedis = jedisPool.getResource();
//        jedis.select(db);
//        String v = jedis.hget(key, field);
//        jedis.close();
//        return v;
//    }
    
    public String hget(String key,String field) {
        Jedis jedis = jedisPool.getResource();
        String v = jedis.hget(key, field);
        jedis.close();
        return v;
    }
    
    public Long hset(String key,String field, String value) {
        Jedis jedis = jedisPool.getResource();
        Long v = jedis.hset(key, field, value);
        jedis.close();
        return v;
    }

//    public Map<String,String> hgetAll(short db, String key) {
//        Jedis jedis = jedisPool.getResource();
//        jedis.select(db);
//        Map<String,String>map = jedis.hgetAll(key);
//        jedis.close();
//        return map;
//    }
    
    public Map<String,String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String,String>map = jedis.hgetAll(key);
        jedis.close();
        return map;
    }

    public void hmset(short db, String key,Map<String,String>map) {
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        jedis.hmset(key, map);
        jedis.close();

    }

    /**
     * 删除redis某个key 固定DB
     * @param db
     * @param key
     */
    public void delDb(short db,String key){
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        jedis.del(key);
        jedis.close();
    }
    /**
     * 删除redis某个key
     * @param key
     */
    public void del(String key){
        Jedis jedis = jedisPool.getResource();
        jedis.del(key);
        jedis.close();
    }
    /**
     * 查询redis数组
     * @param db
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrangeDb(short db, String key, int start, int end){
        List<String> listData = new ArrayList<String>();
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        listData = jedis.lrange(key, start, end);
        jedis.close();
        return listData;
    }
    /**
     * 查询redis数组
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange( String key, int start, int end){
        List<String> listData = new ArrayList<String>();
        Jedis jedis = jedisPool.getResource();
        listData = jedis.lrange(key, start, end);
        jedis.close();
        return listData;
    }


    /**
     * 设置key的活动时间
     * @param db
     * @param key
     * @param expiretime
     */
    public void expire(short db,String key,int expiretime){
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        jedis.expire(key, expiretime);
        jedis.close();
    }

    public void expire(String key,int expiretime){
        Jedis jedis = jedisPool.getResource();
        jedis.expire(key, expiretime);
        jedis.close();
    }

    /**
     * 操作redis中List插入数据,确定DB
     * @param db
     * @param key
     * @param value
     */
    public void lpushDb(short db,String key,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.select(db);
        jedis.lpush(key, value);
        jedis.close();
    }
    /**
     * 操作redis中List插入数据
     * @param key
     * @param value
     */
    public void lpush(String key,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.lpush(key, value);
        jedis.close();
    }

    /**
     * 从redis中取一条数据
     * @param key
     * @return
     */
    public String rpop(String key){
        Jedis jedis = jedisPool.getResource();
        String rpop = jedis.rpop(key);

        jedis.close();
        return rpop;
    }

    /**
     * 操作redis删除List中某个元素
     * @param key
     * @param count
     * @param value
     */
    public void lrem(String key,long count,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.lrem(key,count,value);
        jedis.close();
    }
    /**
     * 操作redis删除List中某个元素 固定DB
     * @param key
     * @param count
     * @param value
     */
    public void lremDb(String key,long count,String value){
        Jedis jedis = jedisPool.getResource();
        jedis.lrem(key,count,value);
        jedis.close();
    }
    /**
     * 获取pipeline对象
     * @param db
     * @return
     */
    public Pipeline getPipeline(short db){
        Pipeline pl;
        Jedis jedis = getJedis();
        jedis.select(db);
        pl = jedis.pipelined();
        return pl;
    }
    
    public boolean exists(String key) {
    	Jedis jedis = jedisPool.getResource();
    	Boolean exists = jedis.exists(key);
    	jedis.close();
    	return exists;
    }

    /**
     * 获取jedis对象
     * @return
     */
    public Jedis getJedis(){
    	return jedisPool.getResource();
    }

}
