package cn.kuroneko.demos.exception;


import cn.kuroneko.demos.vo.IResultCode;

/**
 * 华润万家通业务异常类
 *
 * @author liwei
 * @date 2019/10/22 8:50 PM
 */
public class KuronekoException extends Exception {
    /**
     * 业务服务代码。默认 空字符串
     */
    public static String BIZ_CODE = "";
    /**
     * 错误码
     */
    protected String code;
    /**
     * 异常时的附加信息
     */
    protected String paramMsg;
    /**
     * 抛出异常时，是否在未指定paramMsg的情况下不打印调用栈信息 默认: false.即打印异常信息
     */
    protected boolean turnOffStackTrace = false;

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param code     错误编码
     * @param message  错误消息
     * @param paramMsg 参数名
     * @param cause    上一层异常
     */
    public KuronekoException(String code, String message, String paramMsg, Throwable cause, boolean turnOffStackTrace) {
        super(message, cause);
        this.code = code;
        this.paramMsg = paramMsg;
        this.turnOffStackTrace = turnOffStackTrace;
    }

    public KuronekoException(String code, String message, String paramMsg, Throwable cause) {
        this(code, message, paramMsg, cause, false);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param code     错误编码
     * @param message  错误消息
     * @param paramMsg 异常时的附加信息
     */
    public KuronekoException(String code, String message, String paramMsg) {
        this(code, message, paramMsg, null, false);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param code    错误编码
     * @param message 错误消息
     */
    public KuronekoException(String code, String message) {
        super(message, null);
        this.code = code;
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param resultCode 返回码信息
     * @param paramMsg   参数名
     * @param cause      上一层异常
     */
    public KuronekoException(IResultCode resultCode, String paramMsg, Throwable cause) {
        this(resultCode.getCode(), resultCode.getMsg(), paramMsg, cause);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param resultCode        返回码信息
     * @param paramMsg          参数名
     * @param cause             上一层异常
     * @param turnOffStackTrace true: 全局异常处理器中不打印调用栈信息
     */
    public KuronekoException(IResultCode resultCode, String paramMsg, Throwable cause, boolean turnOffStackTrace) {
        this(resultCode.getCode(), resultCode.getMsg(), paramMsg, cause, turnOffStackTrace);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param resultCode        返回码信息
     * @param paramMsg          参数名
     * @param turnOffStackTrace true: 全局异常处理器中不打印调用栈信息
     */
    public KuronekoException(IResultCode resultCode, String paramMsg, boolean turnOffStackTrace) {
        this(resultCode.getCode(), resultCode.getMsg(), paramMsg, null, turnOffStackTrace);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param resultCode 返回码信息
     * @param paramMsg   参数名
     */
    public KuronekoException(IResultCode resultCode, String paramMsg) {
        this(resultCode.getCode(), resultCode.getMsg(), paramMsg, null);
    }

    /**
     * 华润万家通业务异常类构造方法
     *
     * @param resultCode 返回码信息
     */
    public KuronekoException(IResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMsg(), null);
    }

    public String getCode() {
        return BIZ_CODE + code;
    }

    public String getOriginalCode() {
        return code;
    }

    public String getParamMsg() {
        return paramMsg;
    }

    public boolean isTurnOffStackTrace() {
        return turnOffStackTrace;
    }

    public KuronekoException setTurnOffStackTrace(boolean turnOffStackTrace) {
        this.turnOffStackTrace = turnOffStackTrace;
        return this;
    }
}
