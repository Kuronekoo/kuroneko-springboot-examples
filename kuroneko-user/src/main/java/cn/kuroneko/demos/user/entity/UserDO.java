package cn.kuroneko.demos.user.entity;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * This class was generated by MyBatis Generator.
 * 对应表: test_user 用户
 */
@Data
public class UserDO {
    /**
     * 字段: id 主键
     */
    private Long id;

    /**
     * 字段: name 姓名
     */
    private String name;

    /**
     * 字段: salary 薪水
     */
    private BigDecimal salary;

    /**
     * 字段: age 年龄
     */
    private Integer age;

    /**
     * 字段: groupId 用户所属组织
     */
    private Long groupId;

    /**
     * 字段: createTime 创建时间
     */
    private Date createTime;
}