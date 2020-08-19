package cn.kuroneko.demos.commons.group.web.rest;

import cn.kuroneko.demos.commons.group.dto.UserDTO;
import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.group.client.feign.UserFeignClient;
import cn.kuroneko.demos.commons.group.client.rest.WxClient;
import cn.kuroneko.demos.commons.manage.RedisValueService;
import cn.kuroneko.demos.commons.service.LockService;
import cn.kuroneko.demos.commons.threads.NamedBasicThreadFactory;
import cn.kuroneko.demos.commons.utils.ResultUtils;
import cn.kuroneko.demos.commons.utils.VerifyUtils;
import cn.kuroneko.demos.commons.vo.Result;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 09:01
 **/
@RestController
@RequestMapping("/group-rest")
@Api(tags = "组织 rest接口")
@Slf4j
public class GroupRestController {
    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    WxClient wxClient;

    @Autowired
    RedisValueService redisValueService;

    @Autowired
    LockService lockService;

    /**
     * 分布式锁超时时间
     */
    private int LOCK_TIMEOUT_SECONDS = 120;

    /**
     * 使用线程池，lambda等场景添加trace Id
     */
    private void addLogTraceId() {
        String traceId = UUID.randomUUID().toString();
        MDC.put("X-B3-TraceId", traceId);
    }

    static final int THREAD_CNT = Runtime.getRuntime().availableProcessors() + 1;

    static final ExecutorService EXECUTOR_SERVICE = new ThreadPoolExecutor(THREAD_CNT, THREAD_CNT, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(2048),
            new NamedBasicThreadFactory("KURONEKO-ASYNC-POOL"));


    private static final String BLANK_TOKEN = "";
    private static LoadingCache<String, String> tokenCache;
    private static volatile boolean inited = false;

    /**
     * 使用二级缓存
     *
     * 仅限于更新特别不频繁，但是访问很频繁的数据！！！！。
     * 但如果是本应用的redis，就直接走redis缓存即可
     * 适用场景是需要走feignClient的查询的场景，无法直接访问对方应用的redis，在这里加一层二级缓存可以增加访问效率
     * 这里用token举例其实不太合适，但是示例项目懒得搞了
     * expireAfterAccess: 当缓存项在指定的时间段内没有被读或写就会被回收。
     *
     * expireAfterWrite：当缓存项在指定的时间段内没有更新就会被回收。每次更新之后的指定时间让缓存失效。限制只有1个加载操作时进行加锁，其他请求必须阻塞等待这个加载操作完成；
     * 而且，在加载完成之后，其他请求的线程会逐一获得锁，去判断是否已被加载完成，每个线程必须轮流地走一个“”获得锁，获得值，释放锁“”的过程，这样性能会有一些损耗。
     *
     * refreshAfterWrite：当缓存项上一次更新操作之后的多久会被刷新。性能好一些。如果缓存过期，恰好有多个线程读取同一个key的值，只阻塞加载数据的线程，其余线程返回旧数据。
     * 但没有定时失效机制，在吞吐量很低的情况下，如很长一段时间内没有查询之后，发生的查询有可能会得到一个旧值（这个旧值可能来自于很长时间之前），这将会引发问题。
     */
    @PostConstruct
    public synchronized void init() {
        if (inited) {
            return;
        }
        tokenCache = CacheBuilder.newBuilder()
                //缓存的最大容量
                .maximumSize(10000)
                //超时时间
                .expireAfterWrite(1, TimeUnit.HOURS)
                .softValues()
                .build(new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        if (StringUtils.isBlank(key)) {
                            return BLANK_TOKEN;
                        }
                        //查询传过来的门店所属的公众号
                        String accessToken = wxClient.getAccessToken(key);

                        return StringUtils.isBlank(accessToken)? BLANK_TOKEN : accessToken;
                    }
                });
        log.info("tokenCache init....");
        inited = true;
    }


    /**
     * 查询用户
     * @return
     */
    @GetMapping("/user")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public Result<UserDTO> getUser(@ApiParam(value = "用户姓名", required = true)
                               @RequestParam(value = "name",required = true) String name) throws KuronekoException {
        log.info("name : {}",name);
        VerifyUtils.notBlank(name,"name",true);
        Result<UserDTO> user = userFeignClient.getUser(name);
        return user;
    }

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/token")
    @ApiOperation(value = "查询token", notes = "查询用户")
    public Result<UserDTO> getToken(@ApiParam(value = "token_id", required = true)
                                        @RequestParam(value = "token_id",required = true) String tokenId) throws KuronekoException {
        try {
            String token = tokenCache.get(tokenId);
            //异常或者查询为空的时候,需要手动清除，因为会往二级缓存里面写入BLANK_TOKEN，不清除就满了
            if(BLANK_TOKEN.equals(token)){
                tokenCache.invalidate(tokenId);
            }
        } catch (Exception e) {
            //异常或者查询为空的时候,需要手动清除，因为会往二级缓存里面写入BLANK_TOKEN，不清除就满了
            tokenCache.invalidate(tokenId);
            e.printStackTrace();
        }

        redisValueService.set("token"+System.currentTimeMillis(),System.currentTimeMillis()+"");
        return ResultUtils.successResult();
    }


    /**
     * 查询用户
     * @return
     */
    @DeleteMapping("/clear-chache")
    @ApiOperation(value = "清除缓存", notes = "清除缓存")
    public Result<UserDTO> clearCache(@ApiParam(value = "key", required = true)
                                          @RequestParam(value = "key",required = true) String key,
                                    @ApiParam(value = "timestamp", required = true)
                                        @RequestParam(value = "timestamp",required = true) long timestamp) throws KuronekoException {
        boolean locked = lockService.tryLockDistribute(redisValueService.buildKeyString(key + timestamp), LOCK_TIMEOUT_SECONDS);
        if (!locked) {
            // 没锁住， 说明已经有线程在处理， 或者已经处理过了， 直接返回
            return ResultUtils.successResult();
        }
        EXECUTOR_SERVICE.execute(()->{
            addLogTraceId();
            try {
                Thread.sleep(1000);
                log.info("clear cache!!!!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return ResultUtils.successResult();
    }

}
