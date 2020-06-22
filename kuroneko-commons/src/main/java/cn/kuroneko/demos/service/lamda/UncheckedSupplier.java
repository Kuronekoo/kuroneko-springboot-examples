package cn.com.crv.vwop.spring.boot.support.services.lamda;

import cn.com.crv.vwop.commons.exception.VwopException;

/**
 * Represents a supplier of results.
 * 接受抛CrvsException 的 Supplier接口
 * <p>There is no requirement that a new or distinct result be returned each
 * time the supplier is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #get()}.
 *
 * @author 000264nb01
 * @param <T> the type of results supplied by this supplier
 *
 * @since 1.8
 */
@FunctionalInterface
public interface UncheckedSupplier<T> {
    /**
     * Gets a result.
     *
     * @return a result
     */
    T get() throws VwopException;
}
