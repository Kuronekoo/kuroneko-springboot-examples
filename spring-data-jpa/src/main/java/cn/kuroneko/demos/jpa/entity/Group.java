package cn.kuroneko.demos.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity(name = "test_group")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String groupName;
    /**
     * 标明在user对象中的属性名，LAZY懒加载，EAGER（饥渴加载？）
     * cascade
     * mappedBy将外键的维护权限交给多的一方
     */
    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<User> users;
}
