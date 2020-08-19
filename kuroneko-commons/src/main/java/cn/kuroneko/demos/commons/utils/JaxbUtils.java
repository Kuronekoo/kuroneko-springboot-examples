package cn.kuroneko.demos.commons.utils;


import cn.kuroneko.demos.commons.threads.NamedBasicThreadFactory;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.ref.SoftReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * XML 与java bean 转换工具
 *
 * @author
 * @date 2019/11/20 9:18 PM
 */
public class JaxbUtils {
    /**
     * JAXB上下文使用弱引用，
     */
    private static final ConcurrentHashMap<Class, SoftReference<JAXBContext>> CACHE = new ConcurrentHashMap<>(16);
    private static final Executor EXECUTOR =
            new ThreadPoolExecutor(1, 1, Integer.MAX_VALUE, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                    new NamedBasicThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
    private static final Lock LOCKER = new ReentrantLock();

    /**
     * 反序列化XML
     *
     * @param xml   xml内容字符串
     * @param clazz
     * @param <T>   java bean类型
     * @return
     */
    public static <T> T unmarshal(String xml, Class<T> clazz) throws JAXBException {
        JAXBElement<T> element =
                getContext(clazz).createUnmarshaller().unmarshal(new StreamSource(new StringReader(xml)), clazz);
        return element.getValue();
    }

    /**
     * 将对象转换为XML字符串
     *
     * @param object 不可为NULL
     * @return
     */
    public static String marshal(Object object) {
        return marshal(object, false);
    }

    /**
     * 将对象转换为XML字符串
     *
     * @param object 不可为NULL
     * @param format true: 返回格式化后的xml
     * @return
     * @throws JAXBException
     */
    public static String marshal(Object object, boolean format) {
        if (Objects.isNull(object)) {
            return null;
        }
        JAXBContext context = null;
        if (object instanceof JAXBElement) {
            context = getContext(((JAXBElement<?>) object).getDeclaredType());
        } else {
            Class clazz = object.getClass();
            context = getContext(clazz);
            XmlRootElement xre = (XmlRootElement) clazz.getAnnotation(XmlRootElement.class);
            if (xre == null) {
                object = new JAXBElement(new QName("xml"), clazz, object);
            }
        }

        try {
            StringWriter writer = new StringWriter();
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
            m.marshal(object, new StreamResult(writer));
            return writer.toString();
        } catch (JAXBException e) {
            throw new DataBindingException(e);
        }
    }

    private static JAXBContext getContext(Class clazz) {
        SoftReference<JAXBContext> ref = CACHE.get(clazz);
        JAXBContext context = null;
        boolean needClean = false;
        if (Objects.isNull(ref) || Objects.isNull(ref.get())) {

            needClean = Objects.nonNull(ref);

            try {
                context = JAXBContext.newInstance(clazz);
            } catch (JAXBException e) {
                throw new DataBindingException(e);
            }
            ref = new SoftReference<>(context);
            CACHE.put(clazz, ref);

            if (needClean) {
                EXECUTOR.execute(JaxbUtils::clean);
            }
            return context;
        }

        return ref.get();
    }

    /**
     * 清除无效的引用
     */
    private static void clean() {
        boolean locked = LOCKER.tryLock();
        if (!locked) {
            return;
        }

        try {
            Iterator<Map.Entry<Class, SoftReference<JAXBContext>>> itr = CACHE.entrySet().iterator();
            while (itr.hasNext()) {
                Map.Entry<Class, SoftReference<JAXBContext>> entry = itr.next();
                if (Objects.isNull(entry.getValue()) || Objects.isNull(entry.getValue().get())) {
                    itr.remove();
                }
            }
        } finally {
            LOCKER.unlock();
        }
    }

    /**
     * 移除元素
     *
     * @param clazz
     */
    public static void removeJAXBContextFromCache(Class clazz) {
        CACHE.remove(clazz);
    }

    public static void removeAllJAXBContext() {
        CACHE.clear();
    }

}
