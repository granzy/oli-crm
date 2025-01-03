package cn.oli.crm.security.provider;

import cn.oli.crm.security.service.MyUserDetailService;
import cn.oli.crm.utils.RsaUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码加密认证提供者, 用于解密密码
 * 前端密码使用RSA公钥加密,后端使用私钥解密
 */
public class PasswordEncoreAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordEncoreAuthenticationProvider(MyUserDetailService securityUserService) {
        setUserDetailsService(securityUserService);
    }

    @SneakyThrows
    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            this.logger.debug("Authentication failed: no credentials provided");
            throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        } else {
            String presentedPassword = authentication.getCredentials().toString();
            // 对登录密码进行解密
            presentedPassword = RsaUtil.decryptByPrivateKey(presentedPassword);

            if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                this.logger.debug("Authentication failed: password does not match stored value");
                throw new BadCredentialsException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
            }
        }
    }


}
