package cn.kuroneko.demos.commons.group.mapper;

import cn.kuroneko.demos.commons.group.entity.UserDO;
import cn.kuroneko.demos.commons.group.entity.UserDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserDOMapper {
    /**
     *  根据指定的条件获取数据库记录数:test_user
     *
     * @param example
     */
    long countByExample(UserDOExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录:test_user
     *
     * @param example
     */
    int deleteByExample(UserDOExample example);

    /**
     *  根据主键删除数据库的记录:test_user
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  动态字段,写入数据库记录:test_user
     *
     * @param record
     */
    int insertSelective(UserDO record);

    /**
     *  根据指定的条件查询符合条件的数据库记录:test_user
     *
     * @param example
     */
    List<UserDO> selectByExample(UserDOExample example);

    /**
     *  根据指定主键获取一条数据库记录:test_user
     *
     * @param id
     */
    UserDO selectByPrimaryKey(Long id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录:test_user
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") UserDO record, @Param("example") UserDOExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录:test_user
     *
     * @param record
     */
    int updateByPrimaryKeySelective(UserDO record);
}