package cn.kuroneko.demos.user.web.rest;

import cn.kuroneko.demos.exception.KuronekoException;
import cn.kuroneko.demos.user.dto.UserAddDTO;
import cn.kuroneko.demos.user.dto.UserDTO;
import cn.kuroneko.demos.user.entity.UserDO;
import cn.kuroneko.demos.user.entity.UserDOExample;
import cn.kuroneko.demos.user.mapper.UserDOMapper;
import cn.kuroneko.demos.utils.ResultUtils;
import cn.kuroneko.demos.utils.VerifyUtils;
import cn.kuroneko.demos.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 09:01
 **/
@RestController
@RequestMapping("/user-rest")
@Api(tags = "用户rest接口")
public class UserRestController {
    @Autowired
    UserDOMapper userDOMapper;

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/get")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public UserDTO getUser(@ApiParam(value = "用户姓名", required = true)
                               @RequestParam(value = "name",required = true) String name) throws KuronekoException {
        VerifyUtils.notBlank(name,"name",true);

        UserDOExample example = new UserDOExample();
        example.createCriteria().andNameEqualTo(name);
        List<UserDO> userDOS = userDOMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(userDOS)){
            UserDO userDO = userDOS.get(0);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDO,userDTO);
            return userDTO;
        }
        return null;
    }

}
