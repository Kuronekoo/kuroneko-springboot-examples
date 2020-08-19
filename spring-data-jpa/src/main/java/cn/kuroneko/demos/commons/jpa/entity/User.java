package cn.kuroneko.demos.commons.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name="test_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal salary;
    private Long groupId;
    private Integer age;
    private Date createTime;
    /**
     * 指定外键名称为groupId,ManyToOne这一行不参与insert和update，懒加载
     *      * @JoinColumn 详细解释
     *      * https://blog.csdn.net/u010588262/article/details/76667283
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupId", insertable = false, updatable = false,nullable = true)
    private Group group;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                ", createTime=" + createTime +
                '}';
    }

    public User( User inUser) {
        this.id=inUser.getId();
        this.name = inUser.getName();
        this.salary = inUser.getSalary();
        this.age = inUser.getAge();
        this.groupId=inUser.getGroupId();
        this.createTime = inUser.getCreateTime();
        this.group=null;
    }
}
