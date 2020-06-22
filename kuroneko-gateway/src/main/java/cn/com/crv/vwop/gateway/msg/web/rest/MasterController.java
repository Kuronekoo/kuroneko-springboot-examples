package cn.com.crv.vwop.gateway.msg.web.rest;

import cn.kuroneko.demos.vo.Result;
import cn.kuroneko.demos.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liwei
 * @date 2020/1/16 9:33 PM
 */
@RestController
@Slf4j
public class MasterController {
    @RequestMapping(value = "/")
    public void rootUri() {
        return;
    }
    /**
     * Hystrix  熔断fallback URI
     *
     * @return
     */
    @GetMapping(value = "/fallback")
    public Result<Void> fallback() {
        return ResultUtils.failedResult("G999", "当前服务繁忙，请稍后再试");
    }
}
