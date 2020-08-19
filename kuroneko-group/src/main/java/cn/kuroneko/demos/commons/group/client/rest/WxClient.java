package cn.kuroneko.demos.commons.group.client.rest;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 23:18
 **/
public interface WxClient {
    /**
     * 获取微信accessToken
     * @return
     */
    String getAccessToken(String tokenId);
}
