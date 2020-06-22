package cn.com.crv.vwop.member.config;

import cn.com.crv.vwop.commons.threads.NamedBasicThreadFactory;
import cn.com.crv.vwop.member.config.property.CustomerMsgProperty;
import cn.com.crv.vwop.member.config.property.WrappedNcmsProperty;
import cn.com.crv.vwop.member.service.HttpService;
import cn.com.crv.vwop.member.service.impl.HttpServiceImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liwei
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

    // @Bean(initMethod = "initEsbRequestTemplate")
    // @ConfigurationProperties(prefix = "vwop.esb")
    // public NcmsProperty ncmsProperty() {
    //     NcmsProperty ncmsProperty = new NcmsProperty();
    //     // ncmsProperty.initEsbRequestTemplate();
    //     return ncmsProperty;
    // }

    @Bean(initMethod = "init")
    @ConfigurationProperties(prefix = "vwop.esb")
    public WrappedNcmsProperty wrappedNcmsProperty() {
        WrappedNcmsProperty wrappedNcmsProperty = new WrappedNcmsProperty();
        return wrappedNcmsProperty;
    }

    @Bean
    @ConfigurationProperties(prefix = "vwop.msg")
    public CustomerMsgProperty customerMsgProperty() {
        return new CustomerMsgProperty();
    }

    @Bean
    public HttpService httpService() throws Exception {
        HttpServiceImpl httpService=new  HttpServiceImpl();
        httpService.init();
        return httpService;
    }

}
