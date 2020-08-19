package cn.kuroneko.demos.commons.manage.impl;

import cn.kuroneko.demos.commons.manage.RedisValueService;
import cn.kuroneko.demos.commons.utils.GsonHelper;
import cn.kuroneko.demos.commons.vo.RedisResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author liwei
 * @date 18/3/1 下午5:15
 */
public class DefaultRedisValueServiceImpl implements RedisValueService {
    private static final Logger logger = LoggerFactory.getLogger(DefaultRedisValueServiceImpl.class);

    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOperations;

    private GeoOperations<String, String>  geoOperations;
    /**
     * 默认超时时间
     */
    private long defaultTimeout = DEFAULT_EXPIRE_TIME;

    private static final Gson GSON = GsonHelper.GSON;

    public DefaultRedisValueServiceImpl(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
        this.geoOperations = redisTemplate.opsForGeo();
    }

    @Override
    public void geoRemove(String key){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        geoOperations.remove(key);
    }

    @Override
    public void geoAdd(String key,double longitude,double latitude,String member){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        geoOperations.add(key,new Point(longitude,latitude),member);
    }

    @Override
    public void geoAddMap(String key,Map<String,Point> map){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        geoOperations.add(key,map);
    }

    @Override
    public List<Point> geoGet(String key ,String member){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        return geoOperations.position(key,member);
    }



    @Override
    public GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String key,double longitude,double latitude,
                                                                      double distance,Metrics metrics,long count){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if(Objects.isNull(metrics)){
            metrics = Metrics.KILOMETERS;
        }
        Point center = new Point(longitude, latitude);
        Distance within = new Distance(distance, metrics);
        Circle circle = new Circle(center,within);
        RedisGeoCommands.GeoRadiusCommandArgs args =
                RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        args.sortAscending().limit(count).includeDistance().includeCoordinates();
        return geoOperations.radius(key, circle,args);
    }

    public void watch(String key){
    }

    @Override
    public Long getKeyExpireTime(String key){
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return 0L;
        }
        return this.valueOperations.getOperations().getExpire(key,TimeUnit.SECONDS);
    }

    @Override
    public String get(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return StringUtils.EMPTY;
        }
        return this.valueOperations.get(key);
    }

    @Override
    public <T> void get(String key, Type type, RedisResult<T> result) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        if (type == null) {
            logger.warn("Params clazz is null!");
            return;
        }
        if (result == null) {
            logger.warn("Params result is null!");
            return;
        }
        String value = get(key);
        if (StringUtils.isBlank(value)) {
            result.setExist(false);
            return;
        }
        result.setExist(true);
        if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return;
        }
        T obj = null;
        try {
            obj = GSON.fromJson(value, type);
        } catch (JsonSyntaxException e) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", type.getTypeName(), value);
            //此时视为无数据，依赖业务重新刷正确的数据到缓存
            result.setExist(false);
        }

        result.setResult(obj);
    }

    @Override
    public <T> RedisResult<T> getResult(String key, Type type) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if (type == null) {
            logger.warn("Params clazz is null!");
            return null;
        }
        RedisResult<T> redisResult = new RedisResult<T>();
        this.get(key, type, redisResult);
        return redisResult;
    }

    @Override
    public void set(String key, String value) {
        set(key, value, defaultTimeout, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        if (timeout < 0) {
            this.valueOperations.set(key, value);
        } else {
            this.valueOperations.set(key, value, timeout, timeUnit);
        }
    }

    @Override
    public void set(ValueOperations<String, String> valueOperations, String key, String value, long timeout,
            TimeUnit timeUnit) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("ValueOperations should not be null!");
        }
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        if (timeout < 0) {
            valueOperations.set(key, value);
        } else {
            valueOperations.set(key, value, timeout, timeUnit);
        }
    }

    @Override
    public Boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(key)) {
            throw new IllegalArgumentException("Params key is blank!");
        }
        Boolean result ;
        if (timeout <= 0) {
            result = this.valueOperations.setIfAbsent(key, value);
        } else {
            result = this.valueOperations.setIfAbsent(key, value, timeout, timeUnit);
        }

        return result;
    }

    @Override
    public String getAndSet(String key, String value, long timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        String old = this.valueOperations.getAndSet(key, value);
        if (timeout >= 0) {
            redisTemplate.expire(key, timeout, timeUnit);
        }
        return old;
    }

    @Override
    public void delete(Collection<String> keys) {

        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("Params keys is null or 0 length!");
            return;
        }
        redisTemplate.delete(keys);
    }

    @Override
    public void delete(String key) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params keys is null or 0 length!");
            return;
        }
        redisTemplate.delete(key);
    }

    /**
     * 批量set缓存数据
     *
     * @param keyValues Map形式
     * @param timeout   小于0时，表示永久保存
     * @param timeUnit  时间单位
     */
    @Override
    public void mset(final Map<String, String> keyValues, long timeout, TimeUnit timeUnit) {
        if (keyValues == null || keyValues.isEmpty() || timeUnit == null) {
            return;
        }

        redisTemplate.executePipelined((RedisCallback<Void>) (connection) -> {
            connection.openPipeline();
            Map<byte[], byte[]> rawData = new LinkedHashMap<>();
            keyValues.forEach((key, value) -> {
                rawData.put(key.getBytes(DEFAULT_DEFAULT_CHARSET), value.getBytes(DEFAULT_DEFAULT_CHARSET));
            });
            connection.mSet(rawData);
            if (timeout < 0) {
                return null;
            }
            long seconds = TimeUnit.SECONDS.convert(timeout, timeUnit);
            rawData.forEach((key, value) -> {
                connection.expire(key, seconds);
            });
            connection.closePipeline();
            return null;
        });

    }

    @Override
    public boolean exists(String key) {
        if (StringUtils.isBlank(key)) {
            //不接受空值
            return false;
        }
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long incrBy(String key, long value) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        return this.valueOperations.increment(key, value);
    }

    @Override
    public void setTTL(String key, int timeout, TimeUnit timeUnit) {
        if (StringUtils.isBlank(key)) {
            logger.warn("setTTL#Params key is blank!");
            return;
        }
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public Set<String> keys(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return Collections.emptySet();
        }

        return scan(pattern, DEFAULT_SCAN_COUNT);
    }

    /**
     * 扫描对应的key
     *
     * @param keyPattern key字符串，支持通配*
     * @param count      扫描的数量上限
     * @return
     */
    @Override
    public Set<String> scan(String keyPattern, int count) {

        Set<String> execute = Collections.emptySet();
        if (StringUtils.isBlank(keyPattern) || count < 1) {
            return execute;
        }

        execute = redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                Set<String> resultKeys = new LinkedHashSet<>();
                Cursor<byte[]> cursor =
                        connection.scan(ScanOptions.scanOptions().match(keyPattern).count(count).build());

                while (cursor.hasNext()) {
                    resultKeys.add(new String(cursor.next(), Charset.forName("UTF-8")));
                }

                return resultKeys;
            }
        });
        return execute;
    }

    @Override
    public String toJsonString(Object object) {
        if (object == null) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Collection) && CollectionUtils.isEmpty((Collection) object)) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Map) && CollectionUtils.isEmpty((Map) object)) {
            return BLANK_CONTENT;
        }

        return GSON.toJson(object);
    }

    @Override
    public String buildKeyString(Object... objs) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append(KEY_SPLITTER);
            }
            builder.append(obj);
        }

        return builder.toString();
    }

    @Override
    public int deleteWithPattern(String... keys) {
        int deletedCnt = 0;
        if (keys == null || keys.length == 0) {
            return deletedCnt;
        }
        final int threshold = DEFAULT_SCAN_COUNT;
        Set<String> toDeleteKeys = new HashSet<String>();
        for (String key : keys) {
            if (StringUtils.containsIgnoreCase(key, "*")) {
                //若遇通配符，则直接进行批量删除
                Set<String> set = this.scan(key, threshold);
                while (set != null && !set.isEmpty()) {
                    deletedCnt += set.size();
                    this.delete(set);
                    set = this.scan(key, threshold);
                }

            } else {
                toDeleteKeys.add(key);
            }
        }
        if (toDeleteKeys.isEmpty()) {
            return deletedCnt;
        }
        deletedCnt += toDeleteKeys.size();
        this.delete(toDeleteKeys);
        return deletedCnt;
    }

    @Override
    public <T> Map<String, RedisResult<T>> mgetResult(List<String> keys, Type type) {

        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("Param keys is empty or null.");
            return Collections.emptyMap();
        }
        if (type == null) {
            logger.warn("Param type/clazz is null.");
            return Collections.emptyMap();
        }
        Map<String, String> valueMap = mget(keys);

        if (CollectionUtils.isEmpty(valueMap)) {
            //此处不出日志
            return Collections.emptyMap();
        }

        return makeupMgetRawResult(keys, valueMap, type);
    }

    @Override
    public <T> Map<String, RedisResult<T>> makeupMgetRawResult(List<String> keys, Map<String, String> rawResult,
            Type type) {
        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("Param keys is empty or null.");
            return Collections.emptyMap();
        }
        if (type == null) {
            logger.warn("Param type/clazz is null.");
            return Collections.emptyMap();
        }

        if (CollectionUtils.isEmpty(rawResult)) {
            //此处不出日志
            return Collections.emptyMap();
        }

        Map<String, RedisResult<T>> redisResultMap = new LinkedHashMap<>(keys.size());
        //保证与keys原有有顺序一致
        for (int i = 0; i < keys.size(); i++) {

            String key = keys.get(i);
            String value = rawResult.get(key);
            RedisResult<T> redisResult = new RedisResult<T>();

            if (StringUtils.isBlank(value)) {
                redisResult.setExist(false);
                redisResultMap.put(key, redisResult);
                continue;
            }
            redisResult.setExist(true);

            //表示空数据的占位符
            if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
                redisResultMap.put(key, redisResult);
                continue;
            }

            T obj = null;
            try {
                obj = GSON.fromJson(value, type);
            } catch (Exception e) {
                logger.error("Parsing json to Type[{}] failed. ErrorMsg:{}; json content : {}", type.getTypeName(),
                        e.getMessage(), value);
                //此时视为无缓存内容。
                redisResult.setExist(false);
            }
            redisResult.setResult(obj);
            redisResultMap.put(key, redisResult);
        }

        return redisResultMap;

    }

    @Override
    public Map<String, String> mget(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            logger.warn("keys is empty or null.");
            return Collections.emptyMap();
        }
        List<String> strResults = this.valueOperations.multiGet(keys);
        Map<String, String> oriResultMap = new LinkedHashMap<>(keys.size());

        //把取出来的结果 与key一一对应

        if (CollectionUtils.isEmpty(strResults)) {
            keys.forEach(key -> oriResultMap.put(key, null));
            return oriResultMap;
        }
        Iterator<String> itr = strResults.iterator();
        for (String key : keys) {
            if (itr == null || !itr.hasNext()) {
                oriResultMap.put(key, null);
                continue;
            }
            oriResultMap.put(key, itr.next());
            itr.remove();
        }
        return oriResultMap;
    }

    @Override
    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }
}
