package cn.oli.crm.config;

import cn.oli.crm.interceptor.ResponseWrapperInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 * @author csj
 */
@Configuration
@Slf4j
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public ResponseWrapperInterceptor responseResultInterceptor() {
        log.info("响应增强拦截器[ {} ]已初始化...", ResponseWrapperInterceptor.class);
        return new ResponseWrapperInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //响应结果控制拦截
        registry.addInterceptor(responseResultInterceptor());
    }

}
