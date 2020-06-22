package demo.rocketmq.consumer.config;


import lombok.Data;

import java.util.List;

/**
 * rocketMQ基础配置项信息
 *
 * @author kuroneko
 * @date 2019/11/21 12:33 PM
 */
@Data
public class RocketMqProperty {
    /**
     * rocketMQ namespace
     */
    private String namespace;

    /**
     * name server IP:PORT
     */
    private List<String> nameServers;

    /**
     * 消息发送者的组名
     */
    private String producerGroup = "";

    /**
     * 消息消费者的组名
     */
    private String consumerGroup = "";
}
