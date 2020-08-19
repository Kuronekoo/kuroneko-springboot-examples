package demo.rocketmq.consumer.config;
import demo.rocketmq.consumer.config.RocketMqProperty;
import org.apache.rocketmq.client.consumer.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author kuroneko
 * @date 2019/12/25 12:36 PM
 */
@Configuration
public class RocketMqConfig {

    /**
     * MQ TOPIC: 用于处理模版消息
     */
    public final static String TOPIC_LOCAL = "TOPIC_LOCAL";
    /**
     * MQ TAG: 用于处理 推送模板消息到腾讯
     */
    public final static String TAG_LOCAL = "TAG_LOCAL";

    private static final String SPLITTER = ";";

    @Bean
    @ConfigurationProperties(prefix = "rocketmq")
    public RocketMqProperty rocketMqProperty() {
        return new RocketMqProperty();
    }


    /**
     * 此处获取的消息消费者未初始化完成：未注册收到消息后的回调方法
     * push方式被动获取消息，需要初始化监听器
     * 可以返回是否消费成功，稍后消费等
     * @param rocketMqProperty
     * @return
     * @throws MQClientException
     */
    @Bean
    public MQPushConsumer mqConsumer(RocketMqProperty rocketMqProperty) throws MQClientException {
        DefaultMQPushConsumer pushConsumer =
                new DefaultMQPushConsumer(rocketMqProperty.getNamespace(), rocketMqProperty.getConsumerGroup());
        StringBuilder nameSrvAddrBuilder = new StringBuilder();
        rocketMqProperty.getNameServers().forEach(addr -> nameSrvAddrBuilder.append(addr).append(SPLITTER));
        pushConsumer.setNamesrvAddr(nameSrvAddrBuilder.toString());
        pushConsumer.subscribe(TOPIC_LOCAL, TAG_LOCAL);
        return pushConsumer;
    }

    /**
     * rocketMq默认的defaultTopicQueueNums=4，因此消费者数量需要小于等于4，如果希望有更多消费，则需要手动在生产者处增大这个值
     * @param rocketMqProperty
     * @return
     * @throws MQClientException
     */
    @Bean
    public LitePullConsumer mqPullConsumer(RocketMqProperty rocketMqProperty) throws MQClientException{
        DefaultLitePullConsumer pullConsumer =
                new DefaultLitePullConsumer(rocketMqProperty.getNamespace(), "group2",null);
        StringBuilder nameSrvAddrBuilder = new StringBuilder();
        rocketMqProperty.getNameServers().forEach(addr -> nameSrvAddrBuilder.append(addr).append(SPLITTER));
        pullConsumer.setNamesrvAddr(nameSrvAddrBuilder.toString());
        pullConsumer.subscribe(TOPIC_LOCAL, TAG_LOCAL);
        //关闭自动提交offSet
        //默认是自动提交，设置自动提交也是ok的～
        pullConsumer.setAutoCommit(false);
        return pullConsumer;
    }

}
