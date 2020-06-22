package rocketmq.producer.manager;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 15:11
 **/
@Component
public class MqProduceManager {
    @Autowired
    private MQProducer mqProducer;

    public SendResult sendTplMsg(String topic, String tag, String keys, String msgJson)
            throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message msg = null;
        try {
            msg = new Message(topic, tag, keys, msgJson.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            //unreachable
        }
        return mqProducer.send(msg);
    }
}
