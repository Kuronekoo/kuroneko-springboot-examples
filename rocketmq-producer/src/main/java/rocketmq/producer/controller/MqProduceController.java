package rocketmq.producer.controller;

import com.alibaba.fastjson.JSON;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rocketmq.producer.config.RocketMqConfig;
import rocketmq.producer.manager.MqProduceManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-05-25 15:18
 **/
@RestController
public class MqProduceController {
    @Autowired
    MqProduceManager mqProduceManager;

    @GetMapping("/rocket-send")
    public String sendRocket() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "message: testFanoutMessage ";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", messageId);
        map.put("messageData", messageData);
        map.put("createTime", createTime);
        mqProduceManager.sendTplMsg(RocketMqConfig.TOPIC_LOCAL,RocketMqConfig.TAG_LOCAL,"", JSON.toJSONString(map));
        return "ok";
    }
}
