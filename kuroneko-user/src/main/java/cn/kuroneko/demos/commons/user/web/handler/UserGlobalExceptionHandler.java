package cn.kuroneko.demos.commons.user.web.handler;

import cn.kuroneko.demos.commons.handler.GlobalExceptionHandler;
import cn.kuroneko.demos.commons.vo.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest全局异常处理器
 * @author
 * @date 2019/11/17 8:52 PM
 */
@ControllerAdvice
public class UserGlobalExceptionHandler extends GlobalExceptionHandler {

    @Override
    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Result<String>> handlerAllException(HttpServletRequest request, Throwable t) {
        return super.handlerAllException(request, t);
    }
}
