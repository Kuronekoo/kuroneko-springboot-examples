package cn.kuroneko.demos.commons.group.client.feign.impl;

import cn.kuroneko.demos.commons.group.client.feign.UserFeignClient;
import cn.kuroneko.demos.commons.group.dto.UserDTO;
import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.utils.ResultUtils;
import cn.kuroneko.demos.commons.vo.CommonResultCode;
import cn.kuroneko.demos.commons.vo.Result;
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
