package cn.kuroneko.demos.commons.utils;

import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.vo.CommonResultCode;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * 参数格式校验工具类
 * @author liwei
 * Created by liwei
 * 2015/6/22.22:00
 */
public class VerifyUtils {

    /**
     * 正则表达式 数字+英文字母
     */
    public static final String REGX_ALPHABETA_NUM = "^[a-zA-Z0-9]$";

    /**
     * 中国大陆手机号
     */
    public static final String REGX_CN_MOBILE = "^(86|0086|[+]86)?1\\d{10}";

    /**
     * 邮编
     */
    public static final String REGEX_POSTCODE = "^[0-9]{6}$";

    /**
     * 用于校验 0, 1
     */
    public static final String PEGEX_YES_OR_NO = "^[0-1]{1}$";

    /**
     * 正整数校验 正则表达式
     */
    public static final String REGEX_POSITIVE_INT_NUM = "^[0-9]+$";

    /**
     * 整数校验 正则表达式
     */
    public static final String REGEX_INT_NUM = "^-?[0-9]+$";
    public static final String REGEX_DIGTAL = "^-?[0-9]+(\\.\\d+)?";
    public static final String REGEX_IPV4 =
            "^(?:(?:2[0-4][0-9]\\.)|(?:25[0-5]\\.)|(?:1[0-9][0-9]\\.)|(?:[1-9][0-9]\\.)|(?:[0-9]\\.)){3}(?:(?:2[0-5][0-5])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$";
    public static final String REGEX_IPV6 =
            "^((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?$";
    public static final String REGEX_SIMPLE_MOBILE_NO="^1[0-9]{10}";

    public static boolean verifyPhoneNo(String phoneNo, boolean throwException, String fieldName) throws
            KuronekoException {
        notNull(phoneNo, fieldName, true);
        if (phoneNo.matches(REGEX_SIMPLE_MOBILE_NO)) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, fieldName);
        }
        return false;
    }


    public static boolean notBlank(String value, boolean throwException) throws KuronekoException {
        return notBlank(value, "", throwException);
    }

    /**
     * 非空白判断
     *
     * @param value
     * @param paramName
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean notBlank(String value, String paramName, boolean throwException) throws KuronekoException {
        boolean result = StringUtils.isNotBlank(value);
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName);
        } else {
            return false;
        }
    }

    public static boolean greaterThan(int value, int compareValue, String paramName, boolean throwException)
            throws KuronekoException {
        boolean result = value > compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean isPositiveNumber(String value, String paramName, boolean allowBlank, boolean throwException)
            throws KuronekoException {
        return verifyByRegex(value, REGEX_POSITIVE_INT_NUM, paramName, allowBlank, throwException);
    }

    public static boolean isNumber(String value, String paramName, boolean allowBlank, boolean throwException)
            throws KuronekoException {
        return verifyByRegex(value, REGEX_INT_NUM, paramName, allowBlank, throwException);

    }

    public static boolean isDigital(String value, String paramName, boolean allowBlank, boolean throwException)
            throws KuronekoException {
        return verifyByRegex(value, REGEX_DIGTAL, paramName, allowBlank, throwException);
    }


    public static boolean greaterThan(long value, long compareValue, String paramName, boolean throwException)
            throws KuronekoException {
        boolean result = value > compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean greaterThan(double value, double compareValue, String paramName, boolean throwException)
            throws KuronekoException {
        boolean result = value > compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean equalsOrGreaterThan(double value, double compareValue, String paramName,
                                              boolean throwException)
            throws KuronekoException {
        boolean result = value >= compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean equalOrLessThan(int value, int compareValue, String paramName, boolean throwException)
            throws KuronekoException {
        boolean result = value <= compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean equalOrLessThan(long value, long compareValue, String paramName, boolean throwException)
            throws KuronekoException {
        boolean result = value <= compareValue;
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    public static boolean maxStringLength(String value, int maxLength, String paramName, boolean allowBlank,
                                          boolean throwException) throws KuronekoException {
        //非空校验
        boolean isNotBlank = notBlank(value, false);
        if (!isNotBlank) {
            if (allowBlank) {
                return true;
            }
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
            }
            return false;
        }
        //字符串长度校验
        if (value.length() > maxLength) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
            }
            return false;
        }
        return true;
    }

    /**
     * 检查数据最小字符串长度
     *
     * @param value
     * @param minLength
     * @param paramName
     * @param allowBlank
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean minStringLength(String value, int minLength, String paramName, boolean allowBlank,
                                          boolean throwException) throws KuronekoException {
        //非空校验
        if (!notBlank(value, false)) {
            if (allowBlank) {
                return true;
            }
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
            }
            return false;
        }

        //字符串长度校验
        if (value.length() < minLength) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
            }
            return false;
        }
        return true;
    }


    public static boolean contains(int value, int[] container, String paramName, boolean throwException)
            throws KuronekoException {
        if (container == null) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, "container");
        }

        boolean result = ArrayUtils.contains(container, value);
        if (result) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + "=" + value);
        }
        return false;
    }

    /**
     * 非空判断
     *
     * @param obj
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean notNull(Object obj, String paramName, boolean throwException) throws KuronekoException {
        if (obj == null) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + " is null");
            }
            return false;
        }
        return true;
    }

    /**
     * 集合非空判断
     *
     * @param collection
     * @param paramName
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean notEmpty(Collection collection, String paramName, boolean throwException)
            throws KuronekoException {
        if (collection == null || collection.isEmpty()) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + " is empty");
            }
            return false;
        }
        return true;
    }

    /**
     * Map非空判断
     *
     * @param collection
     * @param paramName
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean notEmpty(Map collection, String paramName, boolean throwException) throws KuronekoException {
        if (collection == null || collection.isEmpty()) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, paramName + " is empty");
            }
            return false;
        }
        return true;
    }

    /**
     * 根据正则表达式判断参数合法性
     *
     * @param value
     * @param regex
     * @param fieldName
     * @param allowBlank
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean verifyByRegex(String value, String regex, String fieldName, boolean allowBlank,
                                        boolean throwException)
            throws KuronekoException {
        //非空校验
        if (!notBlank(value, false)) {
            if (allowBlank) {
                return true;
            }
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, fieldName + " is blank!");
            }
            return false;
        }

        if (value.matches(regex)) {
            return true;
        }
        if (throwException) {

            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, fieldName + "=" + value + " is not match with reg-ex:" + regex);
        }
        return false;
    }

    /**
     * 日期字符串校验
     *
     * @param value
     * @param format
     * @param fieldName
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean verifyDate(String value, String format, String fieldName, boolean throwException)
            throws KuronekoException {
        boolean result = notNull(value, fieldName, throwException);
        if (!result) {
            return result;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(value);
        } catch (ParseException e) {
            if (throwException) {
                throw new KuronekoException(CommonResultCode.INVALID_PARAMS, fieldName + "=" + value);
            } else {
                return false;
            }
        }
        String actValue = sdf.format(date);
        if (StringUtils.equals(value, actValue)) {
            return true;
        }
        if (throwException) {
            throw new KuronekoException(CommonResultCode.INVALID_PARAMS, fieldName + "=" + value);
        }
        return false;
    }


    /**
     * 校验IP地址格式，支持IPV4 ，IPV6
     *
     * @param value
     * @param fieldName
     * @param throwException
     * @return
     * @throws KuronekoException
     */
    public static boolean verifyIp(String value, String fieldName, boolean allowBlank, boolean throwException) throws
            KuronekoException {


        String regx = StringUtils.contains(value, ":") ? REGEX_IPV6 : REGEX_IPV4;
        return verifyByRegex(value, regx, fieldName, allowBlank, true);
    }
}
