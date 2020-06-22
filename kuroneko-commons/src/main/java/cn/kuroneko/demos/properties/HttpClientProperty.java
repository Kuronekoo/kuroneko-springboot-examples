package cn.kuroneko.demos.properties;

import lombok.Data;

import java.util.List;

/**
 * http客户端属性配置
 *
 * @author liwei
 * @date 2019/12/4 11:21 AM
 */
@Data
public class HttpClientProperty {

    public static final long DEFAULT_READ_TIMEOUT = 30;
    public static final long DEFAULT_TIMEOUT = 30;

    /**
     * 读数据流的超时时间， 单位秒
     */
    private long readTimeout;

    /**
     * 连接超时时间， 单位秒
     */
    private long connectionTimeout;

    /**
     * true: 使用代理配置
     */
    private boolean enableProxy = false;
    /**
     * 代理地址
     */
    private String proxyHost;

    /**
     * 代理端口
     */
    private int proxyPort;

    /**
     * 总体最大并发请求数
     */
    private int maxRequests;

    /**
     * 每个地址的最大并发请求数
     */
    private int maxRequestsPerHost;

    /**
     * 不使用代理的地址，支持*通配
     */
    private List<String> nonProxyHosts;
}
