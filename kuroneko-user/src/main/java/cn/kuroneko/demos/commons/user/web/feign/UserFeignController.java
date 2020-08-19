package cn.kuroneko.demos.commons.user.web.feign;

import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.user.dto.UserAddDTO;
import cn.kuroneko.demos.commons.user.dto.UserDTO;
import cn.kuroneko.demos.commons.user.entity.UserDO;
import cn.kuroneko.demos.commons.user.entity.UserDOExample;
import cn.kuroneko.demos.commons.user.mapper.UserDOMapper;
import cn.kuroneko.demos.commons.utils.ResultUtils;
import cn.kuroneko.demos.commons.utils.VerifyUtils;
import cn.kuroneko.demos.commons.vo.Result;
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
@RequestMapping("/feign/user")
@Api(tags = "用户feign接口")
public class UserFeignController {
    @Autowired
    UserDOMapper userDOMapper;

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/get")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public Result<UserDTO> getUser(@ApiParam(value = "用户姓名", required = true)
                               @RequestParam(value = "name",required = true) String name) throws KuronekoException {
        VerifyUtils.notBlank(name,"name",true);

        UserDOExample example = new UserDOExample();
        example.createCriteria().andNameEqualTo(name);
        List<UserDO> userDOS = userDOMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(userDOS)){
            UserDO userDO = userDOS.get(0);
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(userDO,userDTO);
            Result<UserDTO> result = ResultUtils.successResult(userDTO);
            return result;
        }
        return ResultUtils.successResult();
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加用户", notes = "添加用户")
    public Result addUser(@RequestBody UserAddDTO dto) throws KuronekoException {
        VerifyUtils.notNull(dto,"UserAddDTO",true);
        UserDO user = new UserDO();
        BeanUtils.copyProperties(dto,user);
        userDOMapper.insertSelective(user);
        return ResultUtils.successResult();
    }
}
