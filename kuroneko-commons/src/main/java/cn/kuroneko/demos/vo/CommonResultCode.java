package cn.kuroneko.demos.vo;


/**
 * 通用返回码
 *
 * @author liwei
 * @date 2019/9/5 5:49 PM
 */
public enum CommonResultCode implements IResultCode {
    /**
     * 请求成功
     */
    SUCCESS("200", "SUCCESS"),
    /**
     * 参数不正确
     */
    INVALID_PARAMS("101", "参数不正确"),
    /**
     * 验签失败
     */
    INVALID_SIGN("102", "验签失败"),
    /**
     * 验签失败: 时间戳为空
     */
    INVALID_SIGN_4_BLANK_TIMESTAMP("103", "验签失败"),

    /**
     * 验签失败: 时间戳 超出当前服务器时间设定的误差值
     */
    INVALID_SIGN_4_EXPIRED_TIMESTAMP("104", "验签失败"),


    /**
     * 验签失败: sign字段为空
     */
    INVALID_SIGN_4_ABSENT("105", "验签失败"),


    /**
     * 正在操作中，请稍后再试。用于获取分布式锁失败的场景
     */
    FORBID_PARALLEL_OPTIONS("900", "正在操作中，请稍后再试"),
    /**
     * 远程调用失败
     */
    REMOTE_FAILED("901", "远程调用失败"),
    /**
     * 系统级异常。系统繁忙，请稍后再试
     */
    SYSTEM_ERROR("999", "系统繁忙，请稍后再试");

    CommonResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 错误码
     */
    public final String code;
    /**
     * 错误信息
     */
    public final String msg;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
