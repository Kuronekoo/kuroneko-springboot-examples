package cn.kuroneko.demos.group.mapper;

import cn.kuroneko.demos.group.entity.GroupDO;
import cn.kuroneko.demos.group.entity.GroupDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface GroupDOMapper {
    /**
     *  根据指定的条件获取数据库记录数:test_group
     *
     * @param example
     */
    long countByExample(GroupDOExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录:test_group
     *
     * @param example
     */
    int deleteByExample(GroupDOExample example);

    /**
     *  根据主键删除数据库的记录:test_group
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  动态字段,写入数据库记录:test_group
     *
     * @param record
     */
    int insertSelective(GroupDO record);

    /**
     *  根据指定的条件查询符合条件的数据库记录:test_group
     *
     * @param example
     */
    List<GroupDO> selectByExample(GroupDOExample example);

    /**
     *  根据指定主键获取一条数据库记录:test_group
     *
     * @param id
     */
    GroupDO selectByPrimaryKey(Long id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录:test_group
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") GroupDO record, @Param("example") GroupDOExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录:test_group
     *
     * @param record
     */
    int updateByPrimaryKeySelective(GroupDO record);
}