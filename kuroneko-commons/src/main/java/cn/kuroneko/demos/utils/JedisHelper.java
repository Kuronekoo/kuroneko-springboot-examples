package cn.com.crv.pos.electric.ticket.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.util.Pool;

import java.util.*;

/**
 * Created by A on 2018/1/29.
 */
@Service
public class JedisHelper {

    private final static Logger logger = LoggerFactory.getLogger(JedisHelper.class);

    @Autowired
    private Pool<Jedis> jedisPool;

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
     *  分布式下redis同步锁实现自增，每天24点自增清零
     *
     */
    public String getListNo(String key, int tryCount) {
        // lockKey锁名
        // 分布式下请求唯一指 可用uuid
        // key不存在时，进行set操作
        // key加的过期设置
        // 过期时间
        Jedis jedis = jedisPool.getResource();
        String lockKey = "somago."+key+".increment.lock";
        String incrementKey = "somago."+key+".increment.number"; // 自增数的key
        String requestId = UUID.randomUUID().toString();
        String ret = null;
        String incrNumber="";
        int count = 0;
        try {
        	// 尝试50次拿锁
        	while (ret == null && count++ < tryCount) {
        		ret= jedis.set(lockKey, requestId, "NX", "PX", 1); // 加锁，1秒后自动释放锁
        	}
            if ("ok".equalsIgnoreCase(ret)) {
                // 加上了锁
                // TODO 自增操作
                if (!jedis.exists(incrementKey)) {
                    int timeInt = DateUtil.getRemainSecondsOneDay(DateUtil.getCurrentDate()); // 获得当天剩余的秒数
                    jedis.setex(incrementKey, timeInt, "0"); // 初始为0，每天24点失效
                }
                jedis.incr(incrementKey);
                incrNumber = jedis.get(incrementKey); // ** 获得自增的数
            } else {
                // 未加锁
                // TODO 考虑是否重复获取锁，或者用其他方式代替自增数
            	return null;
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        } finally {
            if(ret.equalsIgnoreCase("ok")) {
                // 解锁时只能由加锁的requestId来释放锁
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                Object result = jedis.eval(script, Arrays.asList(lockKey), Arrays.asList(requestId));
                if ("ok".equalsIgnoreCase(String.valueOf(result))) {
                    return incrNumber;
                    // 释放了锁
                    // TODO
                } else {
                    // 未释放锁
                    // TODO 考虑是否重复释放
                }
            }
            jedis.close();
        }
        return incrNumber;
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
