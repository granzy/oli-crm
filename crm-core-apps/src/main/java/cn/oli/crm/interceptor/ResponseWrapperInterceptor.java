package cn.oli.crm.interceptor;

import cn.oli.crm.annotation.ResponseGeneric;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 接口响应体控制拦截器
 *
 * @author csj
 */
public class ResponseWrapperInterceptor implements HandlerInterceptor {
    public static final String RESPONSE_RESULT = "RESPONSE-WRAPPER";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 判断是否要开启统一返回结果
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            final Class<?> clazz = handlerMethod.getBeanType();
            final Method method = handlerMethod.getMethod();

            if (clazz.isAnnotationPresent(ResponseGeneric.class)) {
                request.setAttribute(RESPONSE_RESULT, clazz.getAnnotation(ResponseGeneric.class));
            } else if (method.isAnnotationPresent(ResponseGeneric.class)) {
                request.setAttribute(RESPONSE_RESULT, method.getAnnotation(ResponseGeneric.class));
            }
        }
        return true;
    }
}
