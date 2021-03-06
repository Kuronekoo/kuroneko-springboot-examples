package cn.kuroneko.demos.commons.service.lamda;

import cn.kuroneko.demos.commons.exception.KuronekoException;

/**
 * Represents a function that accepts one argument and produces a result.
 * 接受抛CrvsException 的 Function接口
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(Object)}.
 *
 * @author liwei
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 * @since 1.8
 * <p>
 *
 */
@FunctionalInterface
public interface UncheckedFunction<T, R> {
    /**
     * 约定调用方法
     * @param t
     * @return
     * @throws KuronekoException
     */
    R apply(T t) throws KuronekoException;
}
