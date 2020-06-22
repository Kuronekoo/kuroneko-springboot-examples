package cn.kuroneko.demos.service;


import cn.kuroneko.demos.exception.KuronekoException;
import cn.kuroneko.demos.service.lamda.UncheckedSupplier;

/**
 * 分布式锁服务
 *
 * @author liwei
 * 18/03/01 下午7:22
 */
public interface LockService {

    /**
     * 默认分布式锁的超时时间（单位秒）：15分钟
     */
    int DEFAULT_LOCK_TIMEOUT = 900;

    /**
     * 默认分布式锁重试间隔： 100ms
     */
    int DEFAULT_TRY_LOCK_INTERVAL_MILLS = 100;

    /**
     * 可重试分布式分斥锁。根据代码运行耗时情况，可能会出现取锁失败的情况
     *
     * @param key               分布式锁的redis key
     * @param timeout           分布式锁有效过期时长，单位：秒。若超时时间小于1秒，则无法加锁
     * @param retryCount        重试次数。第一次加锁不算在retryCount范围内
     * @param retryIntervalMill 每次重试间隔时间，单位：毫秒。 若小于1ms, 则使用默认间隔时间
     * @param needRandom        true : 间隔时间随机，随机最大值为retryInervalMill， 最小值为retryInervalMill/2
     * @return true: 成功获取分布式锁
     */
    boolean retryLockDistribute(String key, int timeout, int retryCount, int retryIntervalMill, boolean needRandom);

    /**
     * 可重试分布式分斥锁
     *
     * @param key               分布式锁的redis key
     * @param retryCount        重试次数。第一次加锁不算在retryCount范围内
     * @param retryIntervalMill 每次重试间隔时间，单位：毫秒。若小于1ms, 则使用默认间隔时间
     * @param needRandom        true : 间隔时间随机，随机最大值为retryInervalMill， 最小值为retryInervalMill/2
     * @return true: 成功获取分布式锁
     */
    boolean retryLockDistribute(String key, int retryCount, int retryIntervalMill, boolean needRandom);

    /**
     * 可重试分布式分斥锁
     *
     * @param key               分布式锁的redis key
     * @param retryCount        重试次数。第一次加锁不算在retryCount范围内
     * @param retryIntervalMill 每次重试间隔上限时间，单位：毫秒。每次重试的间隔时间取随机取值，范围[retryIntervalMill/2,retryIntervalMill)
     * @return true: 成功获取分布式锁
     */
    boolean retryLockDistribute(String key, int retryCount, int retryIntervalMill);

    /**
     * 根据key尝试获取分布式锁
     *
     * @param key
     * @return true: 成功获取分布式锁
     */
    boolean tryLockDistribute(String key);

    /**
     * 根据key尝试获取分布式锁
     *
     * @param key
     * @param timeout 分布式锁有效过期时长，单位：秒
     * @return true: 成功获取分布式锁
     */
    boolean tryLockDistribute(String key, int timeout);

    /**
     * 根据key 去除分布式锁
     *
     * @param key
     * @return
     */
    void unlockDistribute(String key);

    /**
     * 在分布式锁中执行逻辑 FAST FAILED<br>
     * （默认超时时间)
     *
     * @param key      分布式锁后缀
     * @param supplier 业务逻辑
     * @param <R>      返回值的参数类型
     * @return
     * @throws KuronekoException
     */
    <R> R distributeLockProcess(String key, UncheckedSupplier<R> supplier) throws KuronekoException;

    /**
     * 在分布式锁中执行逻辑 FAST FAILED
     *
     * @param key         分布式锁后缀
     * @param lockTimeout 锁超时时间。单位秒
     * @param supplier    业务逻辑
     * @param <R>         返回值的参数类型
     * @return
     * @throws KuronekoException
     */
    <R> R distributeLockProcess(String key, int lockTimeout, UncheckedSupplier<R> supplier) throws KuronekoException;

}
