package cn.kuroneko.demos.group.client.rest.impl;

import cn.kuroneko.demos.group.client.rest.WxClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 23:19
 **/
@Component
public class WxClientImpl implements WxClient {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String forObject = restTemplate.getForObject(url, String.class);
        System.out.println(forObject);
        return null;
    }
}
