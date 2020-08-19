package cn.kuroneko.demos.commons.properties;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 22:52
 **/
@Data
public class RedisProperty {
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private long maxWaitMillis;
    private List<String> sentinels;
    private String masterName;
}
