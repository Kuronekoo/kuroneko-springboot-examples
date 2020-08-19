package cn.kuroneko.demos.commons.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用结果数据包装类
 * @author kuroneko
 * 2015/6/22.0:26
 */
@Data
public class Result<T>  implements Serializable {
    private static final long serialVersionUID = 809238049071289823L;


    public <T> Result(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public <T> Result() {

    }

    /**
     * 请求返回码 1成功，其它失败
     */
    private String code;

    /**
     * 错误消息。若code为成功，则本字段为null
     */
    private String msg;

    /**
     * 返回的结果数据
     */
    private T data;


    public Result<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
