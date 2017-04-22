package cn.emac.demo.petstore.controllers;

import cn.emac.demo.petstore.common.JsonResult;
import cn.emac.demo.petstore.common.exceptions.ApiCallException;
import cn.emac.demo.petstore.common.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;

/**
 * NOTE: 不同于Admin后台，对于API服务，即便产生异常，返回的响应码始终应是200
 */
@Slf4j
@ControllerAdvice
public class ApiGlobalController extends AbstractJsonpResponseBodyAdvice {

    private static final String NO_STORE = "no-store";

    public ApiGlobalController() {
        super("callback");
    }

    /**
     * 捕获所有异常
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public JsonResult handleAllException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        return _handleException(request, response, e);
    }

    /**
     * 处理异常的响应
     */
    private JsonResult _handleException(HttpServletRequest request, HttpServletResponse response, Throwable error) {
        response.setHeader(CACHE_CONTROL, NO_STORE);

        // 获取异常源
        while (error.getCause() != null) {
            if (error instanceof ServiceException && !(error.getCause() instanceof ServiceException)) {
                break;
            }
            if (error instanceof ApiCallException && !(error.getCause() instanceof ApiCallException)) {
                break;
            }
            error = error.getCause();
        }

        String errorMsg = error.getMessage();
        log.error("Exception occurred: " + errorMsg + ". [URL=" + request.getRequestURI() + "]", error);

        if (error instanceof MethodArgumentNotValidException) {
            // 数据校验异常
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) error;
            FieldError fieldError = ex.getBindingResult().getFieldError();
            if (StringUtils.isNotBlank(fieldError.getDefaultMessage())) {
                return JsonResult.error(format("%s: %s", fieldError.getField(), fieldError.getDefaultMessage()));
            }
        }
        // 默认返回服务异常
        return JsonResult.error(errorMsg);
    }
}
