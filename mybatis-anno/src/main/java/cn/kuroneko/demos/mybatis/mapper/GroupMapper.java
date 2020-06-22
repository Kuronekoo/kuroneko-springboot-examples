package cn.kuroneko.demos.mybatis.mapper;

import cn.kuroneko.demos.mybatis.entity.Group;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface GroupMapper {
    String querySql="select * from test_group ";

    @Select(querySql+"where id=#{0}")
    @Results({
        @Result(id=true,column = "id",property = "id"),
        @Result(column = "group_name",property = "groupName" ),
        @Result(column = "id",property = "users",
                many=@Many(select = "cn.kuroneko.demos.mybatis.mapper.UserMapper.selectByGroupId",fetchType = FetchType.EAGER))
    })
    Group selectById(Long id);
}
