package cn.oli.crm.handler;

import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.exceptions.BizException;
import cn.oli.crm.model.dto.ParameterInvalidItem;
import cn.oli.crm.model.vo.common.DefaultErrorResult;
import cn.oli.crm.utils.ParameterInvalidItemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理器
 *
 * @author csj
 */
@Slf4j
@RestController
@ControllerAdvice
public class GlobalExceptionHandler {

    /* 处理400类异常 */

    /**
     * 处理违反约束异常（400错误码）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public DefaultErrorResult handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("handleConstraintViolationException start, uri:{}, caused by: ", request.getRequestURI(), e);
        List<ParameterInvalidItem> parameterInvalidItemList = ParameterInvalidItemUtil.convertCVSetToParameterInvalidItemList(e.getConstraintViolations());
        return DefaultErrorResult.failure(BizCodeEnum.PARAM_IS_INVALID, e, HttpStatus.BAD_REQUEST, parameterInvalidItemList);
    }

    /**
     * 处理验证参数封装错误时异常（400错误码）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public DefaultErrorResult handleConstraintViolationException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("handleConstraintViolationException start, uri:{}, caused by: ", request.getRequestURI(), e);
        return DefaultErrorResult.failure(BizCodeEnum.PARAM_IS_INVALID, e, HttpStatus.BAD_REQUEST);
    }

    /**
     * 处理参数绑定时异常（400错误码）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public DefaultErrorResult handleBindException(BindException e, HttpServletRequest request) {
        log.error("handleBindException start, uri:{}, caused by: ", request.getRequestURI(), e);
        List<ParameterInvalidItem> parameterInvalidItemList = ParameterInvalidItemUtil.convertBindingResultToParameterInvalidItemList(e.getBindingResult());
        return DefaultErrorResult.failure(BizCodeEnum.PARAM_IS_INVALID, e, HttpStatus.BAD_REQUEST, parameterInvalidItemList);
    }

    /**
     * 处理使用@Validated注解时，参数验证错误异常（400错误码）
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DefaultErrorResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("handleMethodArgumentNotValidException start, uri:{}, caused by: ", request.getRequestURI(), e);
        List<ParameterInvalidItem> parameterInvalidItemList = ParameterInvalidItemUtil.convertBindingResultToParameterInvalidItemList(e.getBindingResult());
        return DefaultErrorResult.failure(BizCodeEnum.PARAM_IS_INVALID, e, HttpStatus.BAD_REQUEST, parameterInvalidItemList);
    }

    /* 处理自定义异常 */

    /**
     * 处理通用自定义业务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BizException.class)
    public ResponseEntity<DefaultErrorResult> handleBizException(BizException e, HttpServletRequest request) {
        log.error("handleBizException start, uri:{}, exception:{}, caused by: {}", request.getRequestURI(), e.getClass(), e.getMessage());
        DefaultErrorResult defaultErrorResult = DefaultErrorResult.failure(e);
        return ResponseEntity.status(HttpStatus.valueOf(defaultErrorResult.getStatus())).body(defaultErrorResult);
    }

    /* 处理运行时异常 */

    /**
     * 处理未预测到的其他错误（500错误码）
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public DefaultErrorResult handleThrowable(Throwable e, HttpServletRequest request) {
        // :TODO 可通过邮件、微信公众号等方式发送信息至开发人员、记录存档等操作
        log.error("handleThrowable start, uri:{}, caused by: ", request.getRequestURI(), e);
        return DefaultErrorResult.failure(BizCodeEnum.SYSTEM_INNER_ERROR, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
