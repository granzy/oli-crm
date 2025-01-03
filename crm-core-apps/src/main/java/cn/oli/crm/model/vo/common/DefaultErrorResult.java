package cn.oli.crm.model.vo.common;

import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.enums.BizExceptionEnum;
import cn.oli.crm.exceptions.BizException;
import cn.oli.crm.utils.RequestContextUtil;
import cn.oli.crm.utils.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * 默认异常返回
 *
 * @author csj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DefaultErrorResult implements ResponseWrapper {
    /**
     * HTTP响应状态码 {@link HttpStatus}
     */
    private Integer status;

    /**
     * HTTP响应状态码的英文提示
     */
    private String error;

    /**
     * 异常堆栈的精简信息
     */
    private String message;

    /**
     * 系统内部自定义的返回值编码，{@link BizCodeEnum} 它是对错误更加详细的编码
     * tip：spring boot默认返回异常时，该字段为null
     */
    private Integer code;

    /**
     * 调用接口路径
     */
    private String path;

    /**
     * 异常的名字
     */
    private String exception;

    /**
     * 异常的错误传递的数据
     */
    private Object errors;

    /**
     * 时间戳
     */
    private LocalDateTime timestamp;

    public static DefaultErrorResult failure(BizCodeEnum bizCode, Throwable e, HttpStatus httpStatus, Object errors) {
        DefaultErrorResult result = DefaultErrorResult.failure(bizCode, e, httpStatus);
        result.setErrors(errors);
        return result;
    }

    public static DefaultErrorResult failure(BizCodeEnum bizCode, Throwable e, HttpStatus httpStatus) {
        DefaultErrorResult result = new DefaultErrorResult();
        result.setCode(bizCode.code());
        result.setMessage(bizCode.message());
        result.setStatus(httpStatus.value());
        result.setError(httpStatus.getReasonPhrase());
        result.setException(e.getClass().getName());
        result.setErrors(ExceptionUtils.getStackTrace(e));
        String path = RequestContextUtil.getRequest().getRequestURI();
        result.setPath(path);
        result.setTimestamp(LocalDateTime.now());
        return result;
    }

    public static DefaultErrorResult failure(BizException e) {
        BizExceptionEnum ee = BizExceptionEnum.getByEClass(e.getClass());
        if (ee != null) {
            return DefaultErrorResult.failure(ee.getBizCode(), e, ee.getHttpStatus(), e.getData());
        }

        DefaultErrorResult defaultErrorResult = DefaultErrorResult.failure(e.getResultCode() == null ? BizCodeEnum.SUCCESS : e.getResultCode(), e, HttpStatus.OK, e.getData());
        if (StrUtil.isNotEmpty(e.getMessage())) {
            defaultErrorResult.setMessage(e.getMessage());
        }
        return defaultErrorResult;
    }
}
