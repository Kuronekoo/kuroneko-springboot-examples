package cn.kuroneko.demos.commons.manage.impl;

import cn.kuroneko.demos.commons.manage.RocketMqConsumerManager;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * RocketMqConsumerManager默认实现类
 * 到时候使用@Bean的方式装载即可
 * @author
 * @date 2019/11/27 1:51 PM
 */

public class DefaultRocketMqConsumerManager implements RocketMqConsumerManager {
    private static volatile boolean inited = false;

    private MQPushConsumer mqConsumer;

    public DefaultRocketMqConsumerManager(MQPushConsumer mqConsumer) {
        this.mqConsumer = mqConsumer;
    }

    @Override
    public void registerListenerAndInitConsumer(MessageListenerConcurrently messageListenerConcurrently)
            throws MQClientException {
        if (inited) {
            return;
        }
        synchronized (DefaultRocketMqConsumerManager.class) {
            if (inited) {
                return;
            }
            mqConsumer.registerMessageListener(messageListenerConcurrently);
            mqConsumer.start();
            inited = true;
        }
    }

    public MQPushConsumer getMqConsumer() {
        return mqConsumer;
    }

}
