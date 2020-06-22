package cn.kuroneko.demos.test.db;

import cn.kuroneko.demos.mybatis.entity.Group;
import cn.kuroneko.demos.mybatis.entity.Order;
import cn.kuroneko.demos.mybatis.entity.User;
import cn.kuroneko.demos.mybatis.mapper.GroupMapper;
import cn.kuroneko.demos.mybatis.mapper.OrderMapper;
import cn.kuroneko.demos.mybatis.mapper.UserMapper;
import cn.kuroneko.demos.test.MybatisTestApp;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-19 14:31
 **/
public class MapperTest extends MybatisTestApp {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void testMapper(){
        List<Order> orders = orderMapper.selectAll();
        System.out.println(orders);
        Order o1 = Order.builder().orderNumber(UUID.randomUUID().toString()).description(UUID.randomUUID().toString()).build();
        Order o2 = Order.builder().orderNumber(UUID.randomUUID().toString()).description(UUID.randomUUID().toString()).build();
        orderMapper.insert(o1);
        orderMapper.insert(o2);
        //只有在startPage方法后面的第一个mybatisSelect方法会被分页
        PageHelper.startPage(1, 2);
        PageInfo<Order> page = PageInfo.of(orderMapper.selectAll());
        System.out.println(page);


    }

    @Autowired
    UserMapper userMapper;
    @Autowired
    GroupMapper groupMapper;

    @Test
    public void testSelectBySql(){

        userMapper.deleteUser("dd");
        userMapper.deleteUser("dd2");

        User dd = User.builder()
                .name("dd")
                .age(12)
                .salary(new BigDecimal("33"))
                .groupId(1L)
                .build();
        User dd2 = User.builder()
                .name("dd2")
                .age(122)
                .salary(new BigDecimal("332"))
                .groupId(1L)
                .build();

        List<User> list = new ArrayList<>();
        list.add(dd);
        list.add(dd2);
        userMapper.insertBatch(list);


        List<User> users = userMapper.selectByUserName("dd");
        Group group = groupMapper.selectById(1l);
        System.out.println(JSON.toJSONString(users));
        System.out.println(JSON.toJSONString(group));
    }

}
