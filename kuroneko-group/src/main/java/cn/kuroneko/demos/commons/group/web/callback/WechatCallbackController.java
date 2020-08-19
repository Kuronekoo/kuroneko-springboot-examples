package cn.kuroneko.demos.commons.group.web.callback;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;

/**
 * 微信回调控制器
 *
 * @author
 * @date 2019/11/21 6:12 PM
 */
@RestController
@RequestMapping("/wechat")
@Api(tags = "微信回调处理")
@Slf4j
public class WechatCallbackController {
    /**
     * 成功应答
     */
    private static final String RESPONSE_OK = "";

    /**
     * 字节缓存大小
     */
    private static final int BUFFER_SIZE = 512;


    /**
     * 接收微信回调请求
     *
     * @param inputStream
     * @return 如果5秒内处理完成则返回空字符串
     * @throws IOException
     */
    @PostMapping(value = "/callback")
    @ApiOperation(value = "接收微信回调请求", notes = "接收微信回调请求")
    public String wechatMsgCallback(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        byte[] content = new byte[0];
        //读取的字节数
        int cnt = 0;
        //内容总长度
        int length = 0;
        while ((cnt = inputStream.read(buffer)) >= 0) {
            length += cnt;
            byte[] currentContent = content;
            content = new byte[length];
            //复制原内容
            System.arraycopy(currentContent, 0, content, 0, currentContent.length);
            //复制新内容
            System.arraycopy(buffer, 0, content, currentContent.length, cnt);
        }

        String xml = new String(content, "UTF-8");



        return RESPONSE_OK;
    }


}
