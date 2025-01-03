package cn.oli.crm.config;

import cn.oli.crm.filter.JWTAuthenticationFilter;
import cn.oli.crm.filter.LoginFilter;
import cn.oli.crm.filter.SecurityErrorFilter;
import cn.oli.crm.handler.LoginFailureHandler;
import cn.oli.crm.handler.LoginSuccessHandler;
import cn.oli.crm.security.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

/**
 * @author gzy
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Resource
    private MyUserDetailService myUserDetailService;

    @Resource
    private LoginSuccessHandler loginSuccessHandler;

    @Resource
    private LoginFailureHandler loginFailureHandler;

    @Resource
    private LogoutSuccessHandler logoutSuccessHandlerImpl;

    @Resource
    private SecurityErrorFilter securityErrorFilter;

    /**
     * security参数: 拦截规则白名单等
     */
    @Resource
    private PermConfig permConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 不开启跨站请求伪造的防护
        http.csrf(AbstractHttpConfigurer::disable);

        // 这里使用jwt令牌的方式进行认证，不需要session了
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 配置拦截规则
        http.authorizeRequests()
                .antMatchers(permConfig.getWhiteList()).permitAll()
                .anyRequest()
                .authenticated();

        // 退出系统的一些配置，logout 退出系统的url，和成功处理器
        http.logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(logoutSuccessHandlerImpl);

        // 异常统一处理
        http.addFilterBefore(securityErrorFilter, ChannelProcessingFilter.class);

        // 添加自定义的认证过滤器 LoginFilter
        // 前后端登录信息以JSON数据传送，替代Security中的form表单形式
        http.addFilter(loginFilter(http));

        // 添加自定义的过滤器-基本认证过滤器，让每个请求都得经过jwt认证...
        http.addFilter(jwtAuthenticationFilter(http));

        return http.getOrBuild();
    }

    @Bean
    public LoginFilter loginFilter(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(http));
        // 设置认证的url，默认是 /login
        loginFilter.setFilterProcessesUrl("/auth/login");
        // 设置认证成功后的处理器
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        // 设置认证失败后的处理器
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return loginFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(myUserDetailService)
                .passwordEncoder(passwordEncoder())
                .and().getOrBuild();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter(HttpSecurity http) throws Exception {
        return new JWTAuthenticationFilter(authenticationManager(http));
    }

}
