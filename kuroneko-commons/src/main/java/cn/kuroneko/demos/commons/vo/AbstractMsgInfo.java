package cn.kuroneko.demos.commons.vo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 微信消息抽象类
 *
 * @author
 * @date 2019/11/20 8:34 PM
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public abstract class AbstractMsgInfo {

    /**
     * 开发者微信号
     */
    @XmlElement(name = "ToUserName")
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    @XmlElement(name = "FromUserName")
    private String fromUserName;
    /**
     * 消息创建时间（整型）秒级时间戳
     */
    @XmlElement(name = "CreateTime")
    private Long createTime;
    /**
     * 消息类型
     */
    @XmlElement(name = "MsgType")
    private String msgType;

    /**
     * 消息id，64位整型。事件消息的情况下， 本字段为null
     */
    @XmlElement(name = "MsgId")
    private Long msgId;

    /**
     * 公众号原始ID
     */
    private String originalId;

}
