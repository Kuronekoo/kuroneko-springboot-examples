package cn.kuroneko.demos.user.entity;

import java.util.Date;
import lombok.Data;

/**
 * This class was generated by MyBatis Generator.
 * 对应表: t_order 订单
 */
@Data
public class OrderDO {
    /**
     * 字段: id 主键
     */
    private Long id;

    /**
     * 字段: orderNumber 订单号
     */
    private String orderNumber;

    /**
     * 字段: description 描述
     */
    private String description;

    /**
     * 字段: createTime 创建时间
     */
    private Date createTime;
}