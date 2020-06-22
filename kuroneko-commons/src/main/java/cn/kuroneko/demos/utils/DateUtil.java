package cn.kuroneko.demos.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * @description:
 * @author: kuroneko
 * @create: 2020-06-22 22:39
 **/
public class DateUtil {
    public static final DateTimeFormatter yyyyMMddDateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static List<String> getOrderListDateList() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return Arrays.asList(
                dateTimeFormatter.format(now),
                dateTimeFormatter.format(now.minusMonths(1L)),
                dateTimeFormatter.format(now.minusMonths(2L)),
                dateTimeFormatter.format(now.minusMonths(3L))
        );
    }
    public static String getyyyyMMddDateStr(){
        return yyyyMMddDateFormatter.format(LocalDate.now());
    }


}
