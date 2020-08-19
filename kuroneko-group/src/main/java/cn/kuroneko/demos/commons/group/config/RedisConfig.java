package cn.kuroneko.demos.commons.group.config;

import cn.kuroneko.demos.commons.manage.RedisValueService;
import cn.kuroneko.demos.commons.manage.impl.DefaultRedisValueServiceImpl;
import cn.kuroneko.demos.commons.properties.RedisProperties;
import cn.kuroneko.demos.commons.service.LockService;
import cn.kuroneko.demos.commons.service.impl.LockServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

/**
 * redis 配置项
 *
 * @author liwei
 * @date 2019/10/31 4:16 PM
 */
@Configuration
public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "redis")
    public RedisProperties redisProperties(){
        return new RedisProperties();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(redisProperties.getMaxTotal());
        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
        poolConfig.setTestOnBorrow(redisProperties.isTestOnBorrow());
        poolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMills());
        //additional configuration begin ===========================
        poolConfig.setTestWhileIdle(redisProperties.isTestWhileIdle());
        poolConfig.setTestOnReturn(redisProperties.isTestOnReturn());
        poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
        poolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
        poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMillis());
        //additional configuration end   ===========================
        JedisConnectionFactory jcf = null;
        if (redisProperties.isUseSentinelCluster()) {
            jcf = new JedisConnectionFactory(sentinelConfiguration(redisProperties), poolConfig);
        } else {

            // jcf = new JedisConnectionFactory(standaloneConfiguration(redisProperties));

            jcf = new JedisConnectionFactory(poolConfig);
            jcf.setHostName(redisProperties.getStandaloneHost());
            if (!org.springframework.util.StringUtils.isEmpty(redisProperties.getPassword())) {
                jcf.setPassword(redisProperties.getPassword());
            }
            jcf.setPort(redisProperties.getStandalonePort());
            jcf.setDatabase(redisProperties.getDatabase());
        }

        return jcf;
    }

    /**
     * 生成sentinel集群的redis配置
     *
     * @param redisProperties
     * @return
     */
    private RedisSentinelConfiguration sentinelConfiguration(RedisProperties redisProperties) {

        RedisSentinelConfiguration sentinelConfiguration =
                new RedisSentinelConfiguration(redisProperties.getMasterName(),
                        new HashSet<>(redisProperties.getSentinelHosts()));
        if (StringUtils.isNotBlank(redisProperties.getPassword())) {
            sentinelConfiguration.setPassword(redisProperties.getPassword());
        }
        sentinelConfiguration.setDatabase(redisProperties.getDatabase());
        return sentinelConfiguration;
    }

    /**
     * 生成单机的redis配置
     *
     * @param redisProperties
     * @return
     */
    private RedisStandaloneConfiguration standaloneConfiguration(RedisProperties redisProperties) {
        RedisStandaloneConfiguration configuration =
                new RedisStandaloneConfiguration(redisProperties.getStandaloneHost(),
                        redisProperties.getStandalonePort());
        configuration.setPassword(redisProperties.getPassword());
        configuration.setDatabase(redisProperties.getDatabase());
        return configuration;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisValueService redisService(RedisTemplate redisTemplate) {
        RedisValueService redisValueService = new DefaultRedisValueServiceImpl(redisTemplate);
        return redisValueService;
    }

    @Bean
    public LockService lockService(RedisValueService redisService) {
        LockServiceImpl lockService = new LockServiceImpl();
        lockService.setLockPrefix("VWOP_DEMO");
        lockService.setRedisService(redisService);
        return lockService;
    }
}
