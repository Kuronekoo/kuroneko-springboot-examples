package demo.rocketmq.consumer.manage;

import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.consumer.MQConsumer;
import org.apache.rocketmq.client.consumer.MQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 15:11
 **/
@Component
public class MqConsumeManage {

    @Autowired
    private MQPushConsumer mqConsumer;


    private static volatile boolean inited = false;

    public void registerListenerAndInitConsumer(MessageListenerConcurrently messageListenerConcurrently)
            throws MQClientException {
        if (inited) {
            return;
        }
        synchronized (MqConsumeManage.class) {
            if (inited) {
                return;
            }
            mqConsumer.registerMessageListener(messageListenerConcurrently);
            mqConsumer.start();
            inited = true;
        }
    }
}
