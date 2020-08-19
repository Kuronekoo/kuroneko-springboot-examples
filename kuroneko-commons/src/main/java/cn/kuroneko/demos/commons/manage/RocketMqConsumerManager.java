package cn.kuroneko.demos.commons.manage;

import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * rocketMQ 处理
 *
 * @author liwei
 * @date 2019/11/27 1:48 PM
 */
public interface RocketMqConsumerManager {
    /**
     * 初始化订阅者
     */
    void registerListenerAndInitConsumer(MessageListenerConcurrently messageListenerConcurrently)
            throws MQClientException;
}
