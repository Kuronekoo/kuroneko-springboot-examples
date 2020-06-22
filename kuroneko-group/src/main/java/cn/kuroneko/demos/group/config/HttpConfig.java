package cn.kuroneko.demos.group.config;

import cn.kuroneko.demos.properties.HttpClientProperty;
import cn.kuroneko.demos.threads.NamedBasicThreadFactory;
import cn.kuroneko.demos.utils.HttpUtils;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Http 相关配置
 *
 * @author liwei
 * @date 2019/12/4 1:13 PM
 */
@Configuration
public class HttpConfig {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    private BeanFactory beanFactory;

    @Bean
    public TraceableExecutorService traceableExecutorService() {
        ExecutorService executor = new ThreadPoolExecutor(4,
                Runtime.getRuntime().availableProcessors() + 4, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedBasicThreadFactory("HTTP-POOL"));
        return new TraceableExecutorService(this.beanFactory, executor);
    }

    @Bean
    @ConfigurationProperties(prefix = "kuroneko.http")
    public HttpClientProperty httpClientProperty() {
        return new HttpClientProperty();
    }

    @Bean
    public RestTemplate restTemplate(HttpClientProperty httpClientProperty,
            TraceableExecutorService traceableExecutorService) {
        return HttpUtils
                .generateConfiguredRestTemplateWith(restTemplateBuilder, httpClientProperty, traceableExecutorService);
    }

    @Bean
    public OkHttpClient okHttpClient(HttpClientProperty httpClientProperty){
        return HttpUtils.okHttpClient(httpClientProperty);
    }
}
