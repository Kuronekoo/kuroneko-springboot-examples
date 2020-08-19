package cn.kuroneko.demos.commons.manage;

import cn.kuroneko.demos.commons.vo.RedisResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis基础操作服务
 *
 * @author liwei
 * @date 18/3/1 下午5:04
 */
public interface RedisValueService {
    /**
     * REDIS Key 连接符
     */
    String KEY_SPLITTER = ":";
    Charset DEFAULT_DEFAULT_CHARSET = Charset.forName("UTF-8");
    int DEFAULT_SCAN_COUNT = 1000;
    /**
     * NX: 当且仅当缓存中特定的key不存在时设定数据
     */
    String NXXX_SET_IF_NOT_EXISTS = "nx";
    /**
     * XX: 当且仅当缓存中特定的key存在时设定数据
     */
    String NXXX_SET_IF_EXISTS = "xx";

    /**
     * EX:缓存失效的时间单位：秒
     */
    String EXPX_SECONDS = "ex";

    /**
     * EX:缓存失效的时间单位：毫秒
     */
    String EXPX_MILLISECOND = "px";

    /**
     * 默认过期时间 ，3600(秒)
     */
    int DEFAULT_EXPIRE_TIME = 3600;

    /**
     * 空白占位符
     */
    String BLANK_CONTENT = "__BLANK__";

    /**
     * 通配符
     */
    String ANY = "*";

    /**
     * 获取key的过期时间
     * @param key
     * @return
     */
    Long getKeyExpireTime(String key);
    /**
     * 删除一个地理位置数据
     * @param key
     */
    void geoRemove(String key);
    /**
     * 新增一个地理位置数据
     * @param key
     * @param longitude
     * @param latitude
     * @param member
     */
    void geoAdd(String key, double longitude, double latitude, String member);

    /**
     * 用Map形式新增N个地理位置数据
     * @param key
     * @param map
     */
    void geoAddMap(String key, Map<String, Point> map);

    /**
     * 以longitude和latitude维中心点，查找最近distance的地理位置，限制数量为count
     * @param key
     * @param longitude
     * @param latitude
     * @param distance
     * @param metrics
     * @param count
     * @return
     */
    GeoResults<RedisGeoCommands.GeoLocation<String>> geoRadius(String key, double longitude, double latitude,
                                                               double distance, Metrics metrics, long count);

    /**
     * 获取一个地理位置数据
     * @param key
     * @param member
     * @return
     */
    List<Point> geoGet(String key, String member);

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 根据key取对象数据（支持Collection/Map数据类型的type，以及嵌套泛型对象）
     * <p>代码示例</p>
     * <pre>
     * RedisResult&lt;PagedResult&lt;UserBaseInfo>> redisResult = new RedisResult&lt;>();
     * redisValueService.get(k1, new TypeToken&lt;PagedResult&lt;UserBaseInfo>>() {}.getType(), redisResult);
     * </pre>
     *
     * @param key
     * @param type   泛型T的指定类型
     * @param result 不可为null
     */
    <T> void get(String key, Type type, RedisResult<T> result);

    /**
     * 根据key取对象数据（支持Collection/Map数据类型的type，以及嵌套泛型对象）
     *
     * @param key  不可为null
     * @param type 不可为null
     * @param <T>
     * @return
     * @see RedisValueService#get(String, Type, RedisResult)
     */
    <T> RedisResult<T> getResult(String key, Type type);

    /**
     * 写入/修改 缓存内容, 超时时间为默认超时时间
     *
     * @param key
     * @param value
     */
    void set(String key, String value);

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param timeout  缓存内容过期时间，若expireTime小于0 则表示该内容不过期
     * @param timeUnit 时间单位
     */
    void set(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param valueOperations
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    void set(ValueOperations<String, String> valueOperations, String key, String value, long timeout,
             TimeUnit timeUnit);

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param timeout  缓存内容过期时间 ，若expireTime小于0 则表示该内容不过期
     * @param timeUnit 时间单位
     * @return
     */
    Boolean setIfAbsent(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param timeout  缓存内容过期时间 ，若expireTime小于0 则表示该内容不过期
     * @param timeUnit 时间单位
     * @return
     */
    String getAndSet(String key, String value, long timeout, TimeUnit timeUnit);

    /**
     * 根据key删除缓存
     *
     * @param keys
     */
    void delete(Collection<String> keys);

    /**
     * 根据key删除缓存
     *
     * @param key
     */
    void delete(String key);

    /**
     * 批量set缓存数据
     *
     * @param keyValues Map形式
     * @param timeout   小于0时，表示永久保存
     * @param timeUnit  时间单位
     */
    public void mset(final Map<String, String> keyValues, long timeout, TimeUnit timeUnit);

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return 运算结果
     */
    Long incrBy(String key, long value);

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param timeout
     * @param timeUnit
     */
    void setTTL(String key, int timeout, TimeUnit timeUnit);

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*。<br>
     * 最多返回1000条数据
     *  禁止使用该方法
     * @param pattern 如 ke6*abc等
     * @return 返回符合pattern 的key集合 最多返回1000条数据
     */
    Set<String> keys(String pattern);

    /**
     * 将对象转为json字符串。若对象为null，则返回 {@link RedisValueService#BLANK_CONTENT}
     *
     * @param object
     * @return
     */
    String toJsonString(Object object);

    /**
     * 批量删除缓存信息，支持"*"通配
     * 禁止使用该方法
     * @param keys
     * @return 删除的key数量
     */
    int deleteWithPattern(String... keys);

    /**
     * 构建redis key 方法
     *
     * @param objs
     * @return
     */
    String buildKeyString(Object... objs);

    /**
     * 获取redisTemplate对象
     *
     * @return
     */
    RedisTemplate<String, String> getRedisTemplate();

    /**
     * 批量获取redisResult。RedisResult中的泛型占位符可支持Collection和Map类型 <br>
     * 例:
     * <ul>
     * <li>若返回的结果为List的情况，示例代码为：<br>
     * <code style="color:#404040">
     * Map&lt;String, RedisResult&lt;List&lt;UserBaseInfo>>> resultMap =
     * redisValueService.mgetResult(keyList, new TypeToken&lt;List&lt;UserBaseInfo>>(){}.getType());<br>
     * RedisResult&lt;List&lt;UserBaseInfo>> result = resultMap.get(k1);<br>
     * if(result.isExists){<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;List&lt;UserBaseInfo> data = result.getResult();<br>
     * }<br>
     * </code>
     * </li>
     * <li>若返回的结果为Map的情况，同理。调用时Type参数的值需写为：<br>
     * <code style="color:#404040">
     * new TypeToken&lt;Map&lt;String,UserBaseInfo>>(){}.getType()
     * </code>
     * </li>
     * </ul>
     *
     * @param keys redis key 列表
     * @param type 返回的数据类型，需与泛型占位符T的具象类保持一致
     * @param <T>
     * @return
     */
    <T> Map<String, RedisResult<T>> mgetResult(List<String> keys, Type type);

    /**
     * 根据type 参数组装 {@link RedisValueService#mget(List)}原始结果。<br>
     *
     * @param keys
     * @param rawResult
     * @param type
     * @param <T>
     * @return
     */
    <T> Map<String, RedisResult<T>> makeupMgetRawResult(List<String> keys, Map<String, String> rawResult, Type type);

    /**
     * mget 操作
     *
     * @param keys
     * @return Map&lt;key,value from redis&gt;
     */
    Map<String, String> mget(List<String> keys);

    /**
     * 扫描对应的key
     *  禁止使用该方法
     * @param keyPattern key字符串，支持通配*
     * @param count      扫描的数量上限
     * @return
     */
    Set<String> scan(String keyPattern, int count);
}
