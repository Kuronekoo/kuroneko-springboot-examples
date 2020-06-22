package demo.rocketmq.consumer.service;

import demo.rocketmq.consumer.config.RocketMqConfig;
import demo.rocketmq.consumer.manage.MqConsumeManage;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.LitePullConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 15:33
 **/
@Service
public class MqConsumeService implements ApplicationRunner {
    @Autowired
    MqConsumeManage mqConsumeManage;

    @Autowired
    private LitePullConsumer mqPullConsumer;

    @PostConstruct
    public void initConsumer() throws MQClientException {
        mqConsumeManage.registerListenerAndInitConsumer((msgs, context) -> {
            MessageExt currentMsg = null;
            try {
                for (MessageExt msg : msgs) {
                    currentMsg = msg;
                    dealWithMsg(msg);
                }
            } catch (Exception e) {
                String msgBody = "";
                try {
                    msgBody = new String(currentMsg.getBody(), "UTF-8");
                } catch (UnsupportedEncodingException e1) {
                    // do nothing
                }
                e.printStackTrace();
                System.out.println("msgBody : "+msgBody);
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
    }

    /**
     * 处理接收到的消息
     *
     * @param msg
     */
    private void dealWithMsg(MessageExt msg) throws Exception {
        String topic = StringUtils.defaultIfBlank(msg.getTopic(), StringUtils.EMPTY);
        switch (topic) {
            case RocketMqConfig.TOPIC_LOCAL:
                dealLocalMsg(msg);
                break;
            default:
                break;
        }
    }

    private void dealLocalMsg(MessageExt msg) throws UnsupportedEncodingException {
        String content = new String(msg.getBody(), "UTF-8");
        System.out.println(content);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        for (;;) {
            List<MessageExt> msgs = mqPullConsumer.poll();
            for (MessageExt msg : msgs) {
                dealWithMsg(msg);
            }
        }
    }
}
