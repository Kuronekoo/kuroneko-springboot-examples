package cn.kuroneko.demos.commons.vo;

/**
 * 返回码接口
 *
 * @author 孙启金
 * @date 2018/12/25
 */
public interface IResultCode {

    /**
     * 返回码
     *
     * @return
     */
    String getCode();

    /**
     * 返回码描述
     *
     * @return
     */
    String getMsg();
}