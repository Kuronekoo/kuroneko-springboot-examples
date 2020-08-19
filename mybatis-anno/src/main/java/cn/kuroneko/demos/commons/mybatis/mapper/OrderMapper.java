package cn.kuroneko.demos.commons.mybatis.mapper;

import cn.kuroneko.demos.commons.mybatis.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @description:
 * @author: shenchao
 * @create: 2020-06-19 12:00
 **/
public interface OrderMapper {
    String querySql="select * from t_order";

    /**
     * java类和数据字段名称不一样不需要在@Results里面体现，驼峰的才需要
     * @param orderNumber
     * @return
     */
    @Select(querySql + " where order_number = #{0} ")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(column = "order_number", property = "orderNumber"),
            @Result(column = "create_time", property = "createTime")
    })
    Order selectByOrderNumber(String orderNumber);

    /**
     *
     * @param record
     * @return
     */
    @Insert("insert into t_order(order_number,description,create_time) values (#{orderNumber},#{description},now())")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int insert(Order record);



    @Select(querySql)
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(column = "order_number", property = "orderNumber"),
            @Result(column = "create_time", property = "createTime")
    })
    List<Order> selectAll();


    @Update("update t_order set order_number=#{orderNumber},description=#{description} where id = #{id}")
    int update(Order record);

    @Delete("DELETE FROM t_order WHERE id =#{id}")
    void delete(Long id);

}
