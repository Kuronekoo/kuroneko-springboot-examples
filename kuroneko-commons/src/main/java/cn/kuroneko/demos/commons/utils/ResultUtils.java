package cn.kuroneko.demos.commons.utils;

import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.vo.CommonResultCode;
import cn.kuroneko.demos.commons.vo.IResultCode;
import cn.kuroneko.demos.commons.vo.Result;
import org.apache.commons.lang.StringUtils;

/**
 * REST 响应信息工具类
 *
 * @author kuroneko
 * @date 18/3/8 上午11:47
 */
public class ResultUtils {

    public static void checkResult(Result result) throws KuronekoException {
        if (result == null) {
            throw new KuronekoException(CommonResultCode.SYSTEM_ERROR);
        }
        if (!isSuccess(result)) {
            if (StringUtils.isNotBlank(result.getCode()) && StringUtils.isNotBlank(result.getMsg())) {
                throw new KuronekoException(result.getCode(), result.getMsg());
            } else {
                throw new KuronekoException(CommonResultCode.SYSTEM_ERROR);
            }
        }
    }

    public static final <T> Result<T> successResult(T data) {
        return new Result<T>(CommonResultCode.SUCCESS.code, CommonResultCode.SUCCESS.msg).setData(data);
    }

    public static final <T> Result<T> successResult() {
        return new Result<T>(CommonResultCode.SUCCESS.code, CommonResultCode.SUCCESS.msg);
    }

    public static final <T> Result<T> failedResult(IResultCode resultCode) {
        return failedResult(resultCode.getCode(), resultCode.getMsg());
    }

    /**
     * 构造请求失败的结果信息
     *
     * @param resultCode
     * @param bizCode    微服务业务编码。优先级高于 ResultUtils.BIZ_CODE
     * @param <T>
     * @return
     */
    public static final <T> Result<T> failedResult(IResultCode resultCode, String bizCode) {
        return failedResult(resultCode.getCode(), resultCode.getMsg(), bizCode);
    }

    public static final <T> Result<T> failedResult(String code, String msg) {
        return failedResult(code, msg, null);
    }

    public static final <T> Result<T> failedResult(String code, String msg, String bizCode) {

        return new Result<T>(StringUtils.defaultIfBlank(bizCode, StringUtils.EMPTY) + code, msg);
    }

    public static final <T> boolean isSuccess(Result<T> result) {
        return result != null && StringUtils.equalsIgnoreCase(CommonResultCode.SUCCESS.code, result.getCode());
    }
}
