package rocketmq.producer.config;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * 消息生产者，且已初始化完成
     *
     * @param rocketMqProperty
     * @return
     * @throws MQClientException
     */
    @Bean
    public MQProducer mqProducer(RocketMqProperty rocketMqProperty) throws MQClientException {
        DefaultMQProducer producer =
                new DefaultMQProducer(rocketMqProperty.getNamespace(), rocketMqProperty.getProducerGroup());
        StringBuilder nameSrvAddrBuilder = new StringBuilder();
        rocketMqProperty.getNameServers().forEach(addr -> nameSrvAddrBuilder.append(addr).append(SPLITTER));
        producer.setNamesrvAddr(nameSrvAddrBuilder.toString());
        producer.start();
        return producer;
    }

}
