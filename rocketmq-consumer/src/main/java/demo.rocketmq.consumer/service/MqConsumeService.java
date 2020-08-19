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

    /**
     * 只有一个方法可以使用此注释进行注解；
     * 被注解方法不得有任何参数；
     * 被注解方法返回值为void；
     * 被注解方法不得抛出已检查异常；
     * 被注解方法需是非静态方法；
     * 此方法只会被执行一次；
     * 容器加载servlet -> serlet构造函数 -> PostConstruct 注解方法 ->init() -> Service -> destroy() -> PreDestroy 注解
     * @throws MQClientException
     */
//    @PostConstruct
//    public void initConsumer() throws MQClientException {
//        mqConsumeManage.registerListenerAndInitConsumer((msgs, context) -> {
//            MessageExt currentMsg = null;
//            try {
//                for (MessageExt msg : msgs) {
//                    currentMsg = msg;
//                    dealWithMsg(msg);
//                }
//            } catch (Exception e) {
//                String msgBody = "";
//                try {
//                    msgBody = new String(currentMsg.getBody(), "UTF-8");
//                } catch (UnsupportedEncodingException e1) {
//                    // do nothing
//                }
//                e.printStackTrace();
//                System.out.println("msgBody : "+msgBody);
//                //消费失败，稍后再消费
//                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//            }
//            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//        });
//    }

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
        //启动消费者
        mqPullConsumer.start();
        for (;;) {
            List<MessageExt> msgs = mqPullConsumer.poll();
            for (MessageExt msg : msgs) {
                dealWithMsg(msg);
            }
            //消息处理完之后手动提交
            mqPullConsumer.commitSync();
        }
    }
}
