package cn.kuroneko.demos.group.client.feign.impl;

import cn.kuroneko.demos.exception.KuronekoException;
import cn.kuroneko.demos.group.client.feign.UserFeignClient;
import cn.kuroneko.demos.group.dto.UserDTO;
import cn.kuroneko.demos.utils.ResultUtils;
import cn.kuroneko.demos.vo.CommonResultCode;
import cn.kuroneko.demos.vo.Result;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 16:11
 **/
@Component
public class UserFeignClientImpl implements UserFeignClient {
    @Override
    public Result<UserDTO> getUser(String name) throws KuronekoException {
        return ResultUtils.failedResult(CommonResultCode.REMOTE_FAILED.code,CommonResultCode.REMOTE_FAILED.msg+"【会员服务】");
    }
}
