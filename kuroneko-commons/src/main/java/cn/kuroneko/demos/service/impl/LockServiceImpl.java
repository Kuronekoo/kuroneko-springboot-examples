package cn.kuroneko.demos.service.impl;

import cn.kuroneko.demos.exception.KuronekoException;
import cn.kuroneko.demos.manage.RedisValueService;
import cn.kuroneko.demos.service.LockService;
import cn.kuroneko.demos.service.lamda.UncheckedSupplier;
import cn.kuroneko.demos.vo.CommonResultCode;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 锁服务
 *
 * @author liwei
 * 16/10/18 下午7:26
 */

public class LockServiceImpl implements LockService {
    private static final Logger logger = LoggerFactory.getLogger(LockServiceImpl.class);
    private String lockPrefix = "";

    private RedisValueService redisService;

    @Override
    public boolean retryLockDistribute(String key, int timeout, int retryCount, int retryIntervalMill,
            boolean needRandom) {

        if (StringUtils.isBlank(key) || timeout < 1) {
            return false;
        }

        if (retryIntervalMill < 1) {
            retryIntervalMill = DEFAULT_TRY_LOCK_INTERVAL_MILLS;
        }

        boolean locked = this.tryLockDistribute(key, timeout);
        if (retryCount <= 0) {
            return locked;
        }

        int waitTime = retryIntervalMill;
        int remainCount = retryCount;
        while (!locked && remainCount > 0) {
            remainCount--;
            if (needRandom) {
                waitTime = RandomUtils.nextInt(retryIntervalMill / 2) + (retryIntervalMill / 2);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(waitTime);
            } catch (InterruptedException e) {
                //do nothing
            }
            locked = this.tryLockDistribute(key, timeout);
        }

        return locked;
    }

    @Override
    public boolean retryLockDistribute(String key, int retryCount, int retryIntervalMill, boolean needRandom) {
        return this.retryLockDistribute(key, DEFAULT_LOCK_TIMEOUT, retryCount, retryIntervalMill, needRandom);
    }

    @Override
    public boolean retryLockDistribute(String key, int retryCount, int retryIntervalMill) {
        return this.retryLockDistribute(key, retryCount, retryIntervalMill, true);
    }

    /**
     * 根据prefix, key尝试获取分布式锁
     *
     * @param key
     * @return
     */
    @Override
    public boolean tryLockDistribute(String key) {
        return this.tryLockDistribute(key, DEFAULT_LOCK_TIMEOUT);
    }

    /**
     * 根据key尝试获取分布式锁
     *
     * @param key
     * @param timeout 分布式锁有效过期时长，单位：秒
     * @return
     */
    @Override
    public boolean tryLockDistribute(String key, int timeout) {
        if (StringUtils.isEmpty(key)) {
            logger.warn("Params key is empty");
            return false;
        }

        if (timeout < 1) {
            logger.warn("Params timeout should greater than 1 second.");
            return false;
        }

        Boolean result = redisService.setIfAbsent(lockPrefix + key, "1", timeout, TimeUnit.SECONDS);

        return (result != null) && result;
    }

    /**
     * 根据key 去除分布式锁
     *
     * @param key
     * @return
     */
    @Override
    public void unlockDistribute(String key) {
        redisService.delete(lockPrefix + key);
    }

    @Override
    public <R> R distributeLockProcess(String key, UncheckedSupplier<R> supplier) throws KuronekoException {
        return distributeLockProcess(key, DEFAULT_LOCK_TIMEOUT, supplier);
    }

    @Override
    public <R> R distributeLockProcess(String key, int lockTimeout, UncheckedSupplier<R> supplier)
            throws KuronekoException {
        if (Objects.isNull(supplier)) {
            throw new IllegalArgumentException("UncheckedSupplier should not be null.");
        }

        boolean locked = this.tryLockDistribute(key, lockTimeout);
        if (!locked) {
            throw new KuronekoException(CommonResultCode.FORBID_PARALLEL_OPTIONS, "key: " + key);
        }
        try {
            return supplier.get();
        } finally {
            this.unlockDistribute(key);
        }
    }

    public String getLockPrefix() {
        return lockPrefix;
    }

    public void setLockPrefix(String lockPrefix) {
        this.lockPrefix = lockPrefix;
    }

    public RedisValueService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisValueService redisService) {
        this.redisService = redisService;
    }
}
