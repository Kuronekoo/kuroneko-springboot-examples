package cn.kuroneko.demos.commons.handler;

import cn.kuroneko.demos.commons.exception.KuronekoException;
import cn.kuroneko.demos.commons.utils.ResultUtils;
import cn.kuroneko.demos.commons.vo.CommonResultCode;
import cn.kuroneko.demos.commons.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @author
 * @date 18/3/7 上午12:15
 */
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * 所有异常全部走此方法
     *
     * @param request
     * @param t
     * @return
     */
    // @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<Result<String>> handlerAllException(HttpServletRequest request, Throwable t) {

        if (t instanceof KuronekoException) {
            //会花Exception 打印错误码以及错误信息
            KuronekoException he = (KuronekoException) t;
            if (StringUtils.equals(he.getCode(), CommonResultCode.INVALID_PARAMS.code)) {
                log.warn("{} - Parameter is invald:[{}]{} - {}", request.getRequestURL(), he.getCode(),
                        he.getMessage(), he.getParamMsg());
            } else if (StringUtils.isNotBlank(he.getParamMsg())) {
                log.error("{} - Exception occurred:[{}]{} - {}", request.getRequestURL(), he.getCode(),
                        he.getMessage(), he.getParamMsg());
            } else if (he.isTurnOffStackTrace()) {
                log.error("{} - Exception occurred:[{}]{} ", request.getRequestURL(), he.getCode(), he.getMessage());
            } else {
                log.error(request.getRequestURL() + " - Unhandled VwopException:[" + he.getCode() + "]" +
                        he.getMessage(), he);
            }

            return ResponseEntity.status(HttpStatus.OK).body(new Result<String>(he.getCode(), he.getMessage()));
        }

        //未捕获的异常
        logger.error(request.getRequestURL() + " - Unhandled Exception.", t);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResultUtils.failedResult(CommonResultCode.SYSTEM_ERROR,KuronekoException.BIZ_CODE));

    }
}
