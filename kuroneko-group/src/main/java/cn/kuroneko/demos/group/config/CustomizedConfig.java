package cn.kuroneko.demos.group.config;

import cn.kuroneko.demos.threads.NamedBasicThreadFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author
 * @date 2019/11/10 5:09 PM
 */
@Configuration
public class CustomizedConfig {

    @Autowired
    private BeanFactory beanFactory;

    /**
     * 注入可使用sleuth traceId 的线程池
     *
     * @return
     */
    @Bean
    public LazyTraceExecutor lazyTraceExecutor() {
        Executor executor = new ThreadPoolExecutor(4,
                Runtime.getRuntime().availableProcessors() *2 + 4, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new NamedBasicThreadFactory());
        return new LazyTraceExecutor(this.beanFactory, executor);
    }


}
