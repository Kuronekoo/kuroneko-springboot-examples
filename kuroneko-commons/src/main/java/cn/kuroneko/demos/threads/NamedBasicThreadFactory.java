package cn.kuroneko.demos.threads;

import org.apache.commons.lang.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义名称的线程工厂,基础款
 *
 * @author
 * @date 2019/1/22 11:56 AM
 */
public class NamedBasicThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;
    private static final String DEFAULT_PREFIX = "KURONEKO-POOL";

    public NamedBasicThreadFactory(String prefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        if (StringUtils.isBlank(prefix)) {
            prefix = DEFAULT_PREFIX;
        }
        namePrefix = prefix + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public NamedBasicThreadFactory() {
        this(DEFAULT_PREFIX);
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
