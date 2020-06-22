package cn.kuroneko.dems.test.xmlbatis.mapper;

import cn.kuroneko.demos.xmlbatis.entity.UserDO;
import cn.kuroneko.demos.xmlbatis.entity.UserDOExample;
import cn.kuroneko.demos.xmlbatis.mapper.UserDOMapper;
import cn.kuroneko.dems.test.xmlbatis.XmlBatisTestApp;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-19 17:19
 **/
public class MapperTests  extends XmlBatisTestApp {

    @Autowired
    UserDOMapper userDOMapper;

    @Test
    public void testMapper(){
        UserDOExample example = new UserDOExample();
        example.createCriteria().andNameEqualTo("五更瑠璃");
        userDOMapper.deleteByExample(example);


        UserDO userDO = new UserDO();
        userDO.setAge(1);
        userDO.setName("五更瑠璃");
        userDOMapper.insertSelective(userDO);

        List<UserDO> userDOS = userDOMapper.selectByExample(example);
        System.out.println(userDOS);
    }
}
