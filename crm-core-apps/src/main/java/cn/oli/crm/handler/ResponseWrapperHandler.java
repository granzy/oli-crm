package cn.oli.crm.handler;

import cn.oli.crm.annotation.ResponseGeneric;
import cn.oli.crm.interceptor.ResponseWrapperInterceptor;
import cn.oli.crm.model.vo.common.DefaultErrorResult;
import cn.oli.crm.model.vo.common.ResponseResult;
import cn.oli.crm.model.vo.common.ResponseWrapper;
import cn.oli.crm.utils.JsonUtil;
import cn.oli.crm.utils.RequestContextUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * 接口响应体处理器
 *
 * @author csj
 */
@ControllerAdvice
public class ResponseWrapperHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        Object target = RequestContextUtil.getRequest().getAttribute(ResponseWrapperInterceptor.RESPONSE_RESULT);
        boolean needWrapper = true;
        if (!Objects.isNull(target)) {
            ResponseGeneric responseGeneric = (ResponseGeneric) target;
            needWrapper = responseGeneric.enabled();
        }
        return needWrapper;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object target = RequestContextUtil.getRequest().getAttribute(ResponseWrapperInterceptor.RESPONSE_RESULT);
        Class<? extends ResponseWrapper> targetClazz;
        boolean needWrapper = true;
        if (Objects.isNull(target)) {
            targetClazz = ResponseWrapper.class;
        } else {
            ResponseGeneric responseGeneric = (ResponseGeneric) target;
            targetClazz = responseGeneric.value();
            needWrapper = responseGeneric.enabled();
        }
        String requestUri = RequestContextUtil.getRequest().getRequestURI();
        boolean isSwagger = requestUri.contains("swagger") || requestUri.contains("api-docs");
        boolean isActuator = requestUri.contains("/actuator/");
        if (targetClazz.isAssignableFrom(ResponseWrapper.class) && needWrapper && !isSwagger && !isActuator) {
            if (body instanceof DefaultErrorResult) {
                DefaultErrorResult errorResult = (DefaultErrorResult) body;
                if (errorResult.getStatus() == HttpStatus.OK.value()) {
                    return ResponseResult.builder().code(errorResult.getCode()).msg(errorResult.getMessage()).data(errorResult.getErrors()).build();
                } else {
                    return body;
                }
            } else if (body instanceof String) {
                return JsonUtil.object2Json(ResponseResult.success(body));
            }
            return body == null ? null : ResponseResult.success(body);
        }

        return body;
    }
}
