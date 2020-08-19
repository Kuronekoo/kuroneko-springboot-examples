package cn.kuroneko.demos.commons.group.client.feign;

import cn.kuroneko.demos.commons.group.dto.UserDTO;
import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.vo.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 16:06
 **/
@FeignClient("kuroneko-user")
public interface UserFeignClient {
    /**
     * 查询用户
     * getMapping 中需要输入完整路径
     * @return
     */
    @GetMapping("/feign/user/get")
    @ApiOperation(value = "查询用户", notes = "查询用户")
    Result<UserDTO> getUser(@ApiParam(value = "用户姓名", required = true)
                           @RequestParam(value = "name",required = true) String name) throws KuronekoException;
}
