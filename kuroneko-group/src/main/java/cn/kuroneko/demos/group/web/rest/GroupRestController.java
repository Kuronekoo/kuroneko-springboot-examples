package cn.kuroneko.demos.group.web.rest;

import cn.kuroneko.demos.exception.KuronekoException;
import cn.kuroneko.demos.group.client.feign.UserFeignClient;
import cn.kuroneko.demos.group.client.rest.WxClient;
import cn.kuroneko.demos.group.dto.UserDTO;
import cn.kuroneko.demos.group.entity.UserDO;
import cn.kuroneko.demos.group.entity.UserDOExample;
import cn.kuroneko.demos.group.mapper.UserDOMapper;
import cn.kuroneko.demos.utils.ResultUtils;
import cn.kuroneko.demos.utils.VerifyUtils;
import cn.kuroneko.demos.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/group-rest")
@Api(tags = "组织 rest接口")
@Slf4j
public class GroupRestController {
    @Autowired
    UserFeignClient userFeignClient;

    @Autowired
    WxClient wxClient;

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/user")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public Result<UserDTO> getUser(@ApiParam(value = "用户姓名", required = true)
                               @RequestParam(value = "name",required = true) String name) throws KuronekoException {
        log.info("name : {}",name);
        VerifyUtils.notBlank(name,"name",true);
        Result<UserDTO> user = userFeignClient.getUser(name);
        return user;
    }

    /**
     * 查询用户
     * @return
     */
    @GetMapping("/token")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public Result<UserDTO> getToken() throws KuronekoException {
        wxClient.getAccessToken();
        return ResultUtils.successResult();
    }


}
