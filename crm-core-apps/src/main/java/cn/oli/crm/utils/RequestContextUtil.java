package cn.oli.crm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Web容器servlet上下文获取
 *
 * @author csj
 */
@Component
public class RequestContextUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
    }

    public static ServletContext getServletContext() {
        return Objects.requireNonNull(ContextLoader.getCurrentWebApplicationContext()).getServletContext();
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RequestContextUtil.applicationContext = applicationContext;
    }
}
