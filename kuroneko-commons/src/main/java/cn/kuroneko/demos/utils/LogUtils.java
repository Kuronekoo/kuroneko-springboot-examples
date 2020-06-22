package cn.com.crv.pos.electric.ticket.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

/**
 * 日志处理工具类
 *
 * @author fuxiao9@crv.com.cn
 * 2016/9/14.
 */
public class LogUtils {

    /**
     * 将异常堆堆栈跟踪信息转字符串
     *
     * @param e 异常对象
     * @return 异常堆栈字符串
     */
    public static String stackTraceAsString(final Throwable e) {
        String str = "";
        try (StringWriter sw = new StringWriter()) {
            e.printStackTrace(new PrintWriter(sw, true));
            str = sw.toString();
            sw.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return str;
    }

    /**
     * 打印客户端Http请求参数信息
     *
     * @param request HttpServletRequest
     * @return String
     */
    public static String printRequestInfo(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("=============== Headers begin ================\n");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            sb.append(" Header = ");
            sb.append(header);
            sb.append(" : ");
            sb.append(request.getHeader(header));
            sb.append("\n");
        }
        sb.append("=============== Headers end ================\n");
        sb.append("=============== Parameter begin ================\n");
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String key = params.nextElement();
            sb.append(" key = ");
            sb.append(key);
            sb.append(" : ");
            sb.append(request.getParameter(key));
            sb.append("\n");
        }
        sb.append("=============== Parameter end ================\n");
        return sb.toString();
    }
}
