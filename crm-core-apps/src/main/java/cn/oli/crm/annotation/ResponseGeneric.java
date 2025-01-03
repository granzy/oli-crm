package cn.oli.crm.annotation;

import cn.oli.crm.model.vo.common.ResponseResult;
import cn.oli.crm.model.vo.common.ResponseWrapper;

import java.lang.annotation.*;

/**
 * 接口返回结果增强  会通过拦截器拦截后放入标记，在ResponseResultHandler进行结果处理
 *
 * @author csj
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseGeneric {
    // 转换包装类
    Class<? extends ResponseWrapper> value() default ResponseResult.class;

    // 是否开启
    boolean enabled() default true;
}
