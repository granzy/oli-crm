package cn.oli.crm.config;

import cn.oli.crm.security.provider.PasswordEncoreAuthenticationProvider;
import cn.oli.crm.security.service.MyUserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class PasswordEncoreAuthenticationProviderConfig {

    @Resource
    private MyUserDetailService myUserDetailService;

    @Bean
    public PasswordEncoreAuthenticationProvider passwordEncoreAuthenticationProvider() {
        return new PasswordEncoreAuthenticationProvider(myUserDetailService);
    }

}
