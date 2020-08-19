package cn.kuroneko.demos.commons.es.commons;

import cn.kuroneko.demos.commons.vo.IResultCode;

/**
 * 返回码定义
 *
 * @author liwei
 * @date 2019/11/17 8:22 PM
 */
public enum ResultCode implements IResultCode {

    /**
     * 访问ES失败
     */
    ACCESS_ES_FAILED("E001", "访问ES失败"),
    /**
     * 查询条件与聚合运算条件不可同时为空
     */
    SEARCH_ES_INVALID_CONDITION_SUPPLER("E002", "查询条件与聚合运算条件不可同时为空"),
    /**
     * 查询条件与聚合运算条件不可同时为空
     */
    SEARCH_ES_INVALID_CONDITION("E003", "查询条件与聚合运算条件不可同时为空"),
    /**
     * 未找到数据
     */
    SEARCH_ES_DOC_NOT_FOUND("E004", "未找到数据"),
    //
    ;

    ResultCode(String code, String msg) {
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
