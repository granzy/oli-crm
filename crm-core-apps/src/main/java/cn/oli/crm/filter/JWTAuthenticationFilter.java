package cn.oli.crm.filter;

import cn.oli.crm.config.PermConfig;
import cn.oli.crm.constant.Constants;
import cn.oli.crm.enums.SysUserStatusEnum;
import cn.oli.crm.exceptions.TokenInvalidateException;
import cn.oli.crm.exceptions.UserAccessDeniedException;
import cn.oli.crm.exceptions.UserNotExistException;
import cn.oli.crm.model.entity.system.SysUser;
import cn.oli.crm.security.service.MyUserDetailService;
import cn.oli.crm.service.system.SysUserService;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gzy
 */
@Slf4j
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static final String BEARER = "Bearer ";
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Resource
    private SysUserService sysUserService;
    @Resource
    private MyUserDetailService myUserDetailService;
    /**
     * security参数: 拦截规则白名单等
     */
    @Resource
    private PermConfig permConfig;

    public JWTAuthenticationFilter(@Autowired AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws IOException, ServletException {

        for (String pattern : permConfig.getWhiteList()) {
            if (antPathMatcher.match(pattern, request.getServletPath())) {
                chain.doFilter(request, response);
                return;
            }
        }

        String token = request.getHeader("Authorization");
        token = StringUtils.substring(token, BEARER.length());
        log.info("token--------{},request:{}", token, request.getServletPath());

        if (StringUtils.isBlank(token)) {
            throw new TokenInvalidateException();
        }

        // 校验jwt
        boolean verifyToken;
        try {
            verifyToken = JWTUtil.verify(token, Constants.JWT_SECRET.getBytes());
        } catch (Exception e) {
            verifyToken = false;
        }
        if (!verifyToken) {
            throw new TokenInvalidateException();
        }

        JWT jwt = JWTUtil.parseToken(token);
        String account;
        if (jwt.getPayload("account") != null) {
            account = jwt.getPayload("account").toString();
        } else {
            throw new TokenInvalidateException();
        }

        SysUser sysUser = sysUserService.findSysUserByAccountAndDeleteTimeIsNull(account);
        if (sysUser != null) {
            // 上下文信息中不要包含用户密码信息
            sysUser.setPassword("");
            if (SysUserStatusEnum.DISABLED.equals(sysUser.getStatus())) {
                throw new UserAccessDeniedException();
            }
            // 根据查询的用户信息封装成一个Authentication用户认证信息
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(sysUser,
                    "",
                    myUserDetailService.getUserAuthorities(sysUser.getId()));
            // details 信息可以放到这里
            // auth.setDetails();
            // 将得到的用户认证信息填入到上下文中
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw new UserNotExistException();
        }

        // 放行
        chain.doFilter(request, response);
    }

}
