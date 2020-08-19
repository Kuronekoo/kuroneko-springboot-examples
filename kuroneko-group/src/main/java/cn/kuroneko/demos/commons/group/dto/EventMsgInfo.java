package cn.kuroneko.demos.commons.group.dto;

import cn.kuroneko.demos.commons.vo.AbstractMsgInfo;
import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 基本事件消息
 *
 * @author liwei
 * @date 2019/11/20 8:46 PM
 */
@Data
@ToString(callSuper = true)
@XmlAccessorType(XmlAccessType.FIELD)
public class EventMsgInfo extends AbstractMsgInfo {
    /**
     * 事件类型
     */
    @XmlElement(name = "Event")
    private String event;

    /**
     * 事件KEY值
     */
    @XmlElement(name = "EventKey")
    private String eventKey;

    /**
     * 二维码的ticket，可用来换取二维码图片。 带参二维码事件专用
     */
    @XmlElement(name = "Ticket")
    private String ticket;

}
