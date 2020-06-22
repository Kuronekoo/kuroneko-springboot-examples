package cn.kuroneko.demos.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
public class RedisProperties {

    /**
     * redis 地址。useSentinelCluster=true时， 忽略。
     */
    private String standaloneHost="";
    /**
     * redis 端口。useSentinelCluster=true时， 忽略。
     */
    private Integer standalonePort=0;
    private String password;
    private Integer database;
    private boolean useSentinelCluster=false;
    /**
     * sentinel 服务地址信息列表 host:port。 useSentinelCluster=true时有效
     */
    private List<String> sentinelHosts;
    /**
     * master 节点名称（sentinel服务配置文件中可以查看）。 useSentinelCluster=true时有效
     */
    private String masterName;

    private boolean testOnBorrow=false;
    private int maxTotal=20;
    private int maxIdle=5;
    private int maxWaitMills=2000;
    private boolean testWhileIdle = true;
    private boolean testOnReturn = true;
    /**
     * 扫描redis 连接的间隔时间
     */
    private int timeBetweenEvictionRunsMillis = 30000;
    /**
     * 每次扫描时， 扫描多少个连接
     */
    private int numTestsPerEvictionRun = 10;

    /**
     * redis连接空闲时的最存活可时间
     */
    private int minEvictableIdleTimeMillis = 60000;

}
