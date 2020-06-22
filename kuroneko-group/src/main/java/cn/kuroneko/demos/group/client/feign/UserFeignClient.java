package cn.kuroneko.demos.group.client.feign;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 16:06
 **/
@FeignClient("kuroneko-user")
public class UserFeignClient {
}
