package cn.kuroneko.demos.commons.group.mapper;

import cn.kuroneko.demos.commons.group.entity.OrderDO;
import cn.kuroneko.demos.commons.group.entity.OrderDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrderDOMapper {
    /**
     *  根据指定的条件获取数据库记录数:t_order
     *
     * @param example
     */
    long countByExample(OrderDOExample example);

    /**
     *  根据指定的条件删除数据库符合条件的记录:t_order
     *
     * @param example
     */
    int deleteByExample(OrderDOExample example);

    /**
     *  根据主键删除数据库的记录:t_order
     *
     * @param id
     */
    int deleteByPrimaryKey(Long id);

    /**
     *  动态字段,写入数据库记录:t_order
     *
     * @param record
     */
    int insertSelective(OrderDO record);

    /**
     *  根据指定的条件查询符合条件的数据库记录:t_order
     *
     * @param example
     */
    List<OrderDO> selectByExample(OrderDOExample example);

    /**
     *  根据指定主键获取一条数据库记录:t_order
     *
     * @param id
     */
    OrderDO selectByPrimaryKey(Long id);

    /**
     *  动态根据指定的条件来更新符合条件的数据库记录:t_order
     *
     * @param record
     * @param example
     */
    int updateByExampleSelective(@Param("record") OrderDO record, @Param("example") OrderDOExample example);

    /**
     *  动态字段,根据主键来更新符合条件的数据库记录:t_order
     *
     * @param record
     */
    int updateByPrimaryKeySelective(OrderDO record);
}