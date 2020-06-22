package cn.kuroneko.demos.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * redis 中取得的结果
 *
 * @author liwei
 * @date 2017/2/7
 */
@Data
public class RedisResult<T> implements Serializable {

    private static final long serialVersionUID = 2362888117172021946L;
    /**
     * redis中是否存在
     */
    private boolean exist = false;

    /**
     * redis中取得的数据
     */
    private T result;


}
