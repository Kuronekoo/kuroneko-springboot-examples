package test.db;

import cn.kuroneko.demos.commons.jpa.entity.Group;
import cn.kuroneko.demos.commons.jpa.entity.User;
import cn.kuroneko.demos.commons.jpa.repository.GroupRepsitory;
import cn.kuroneko.demos.commons.jpa.repository.UserRepository;
import cn.kuroneko.demos.commons.jpa.specification.MySpecification;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import test.JpaTestApp;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public class JpaTests extends JpaTestApp {
    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepsitory groupRepsitory;

    @Test
    public void testDDl(){
        User user = User.builder()
                .name("张三")
                .salary(new BigDecimal("23.00"))
                .age(12)
                .createTime(new Date())
                .build();
        User save = userRepository.save(user);
        System.out.println(save.getId());
        List<User> all = userRepository.findAll();
        System.out.println(all);
        userRepository.deleteInBatch(all);
    }

    @Test
    public void testInsert() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        User user = User.builder()
                .name("张三")
                .salary(new BigDecimal("23.00"))
                .age(12)
                .createTime(simpleDateFormat.parse("2019-01-29"))
                .build();
        User user2 = User.builder()
                .name("李四")
                .salary(new BigDecimal("56.00"))
                .age(15)
                .createTime(simpleDateFormat.parse("2019-01-28"))
                .build();
        User user3 = User.builder()
                .name("王五")
                .salary(new BigDecimal("99.00"))
                .age(99)
                .createTime(simpleDateFormat.parse("2019-01-27"))
                .build();
        List<User> list = new ArrayList<>();
        list.add(user);
        list.add(user2);
        list.add(user3);
        userRepository.saveAll(list);
    }
    @Test
    public void testSelectExample(){
        User user = User.builder()
                .name("张三")
                .build();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(0, 2, sort);
        Page<User> all = userRepository.findAll(Example.of(user), pageable);
        Page<User> all2 = userRepository.findAll( pageable);
        System.out.println(all);
        System.out.println("---->"+new Gson().toJson(all2));
    }
    @Test
    public void testSelectSpecification() throws ParseException {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Date date = getyyyymmddDate("2019-01-01");
        String name = "张三";
        Pageable pageable = PageRequest.of(0, 2, sort);
//        Specification<User> specification = new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return null;
//            }
//        };
//        Specification<User> specification=(root,query,cb)->{
//            Predicate predicate  = cb.conjunction();
//            return predicate;
//        };

        Page<User> all = userRepository.findAll(
        (root,query,cb)->{
            Predicate predicate  = cb.conjunction();
            if(null!=date){
                predicate.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createTime"),date));
            }
            if(null!=name){
                predicate.getExpressions().add(cb.like(root.get("name"),String.format("%%%s%%",name)));
            }
            return predicate;
        }
        ,pageable);

        System.out.println(all);
    }

    @Test
    public void testSelectSpecification2() throws ParseException {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        String name = "张三";
        Pageable pageable = PageRequest.of(0, 2, sort);
        Map<String,Object> condition = new HashMap<>();
        condition.put("name",name);
        Page<User> all = userRepository.findAll(new MySpecification<User>(User.class,condition),pageable);
        System.out.println(all);
    }
    public static Date getyyyymmddDate(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(dateStr);
    }

    /**
     * 使用了懒加载的方法要注解@Transactional，否则会报错,使用eager则不需要事务注解
     * 由于使用oneToMany，ManyToOne注解之后会出现循环引用的问题，需要重新构成一下关联的list等数据
     */
    @Test
    @Transactional
    public void testForeignKey(){
        Group group = groupRepsitory.findById(1l).orElse(new Group());
        List<User> collect = group.getUsers().stream().map(User::new).collect(Collectors.toList());
        group.setUsers(collect);
        System.out.println(JSON.toJSONString(group));
    }

    /**
     * 由于使用oneToMany，ManyToOne注解之后会出现循环引用的问题，需要重新构成一下关联的list等数据
     */
    @Test
    @Transactional
    public void testForeignKey1(){
       User user = userRepository.findById(1l).orElse(new User());
       user= new User(user);
        System.out.println(JSON.toJSONString(user));

//        all.forEach(u->{
//            System.out.println(JSON.toJSONString(u));
//        });
    }

}
