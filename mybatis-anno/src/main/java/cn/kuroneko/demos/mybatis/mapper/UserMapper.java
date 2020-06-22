package cn.kuroneko.demos.mybatis.mapper;

import cn.kuroneko.demos.mybatis.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapper{
    String querySql = "select * from test_user ";

    @Select(querySql+"where name = #{name}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(column = "group_id", property = "groupId"),
            @Result(column = "create_time", property = "createTime")
    })
    List<User> selectByUserName(@Param("name") String name);

    @Select(querySql+"where group_id = #{groupId}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(column = "group_id", property = "groupId"),
            @Result(column = "create_time", property = "createTime")
    })
    List<User> selectByGroupId(@Param("groupId") Long groupId);

    @Update("update test_user set name=#{name} where id=#{id}")
    Boolean updateUser(@Param("name") String name, @Param("id") Long id);

    @Delete("delete from test_user where name=#{name}")
    Boolean deleteUser(@Param("name") String name);

    String insertColumn = "INSERT INTO test_user" +
            "(name, salary, age, group_id, create_time)";
    String insertValue =
                    " ("
                    + "#{item.name,jdbcType=VARCHAR}"
                    + ",#{item.salary,jdbcType=DECIMAL}"
                    + ",#{item.age,jdbcType=INTEGER}"
                    + ",#{item.groupId,jdbcType=INTEGER}"
                    + ",sysdate()"
                    + ")";

    @Insert("<script>" + insertColumn
            + " values "
            + " <foreach collection = \"list\" item=\"item\" index= \"index\" separator =\",\"> "
            + insertValue
            + " </foreach > "
            + "</script>")
    Integer insertBatch(List<User> orderDiscs);


}
