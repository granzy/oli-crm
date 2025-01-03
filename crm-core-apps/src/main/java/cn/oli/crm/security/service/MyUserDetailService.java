package cn.oli.crm.security.service;

import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.enums.SysUserStatusEnum;
import cn.oli.crm.exceptions.BizException;
import cn.oli.crm.model.entity.system.SysUser;
import cn.oli.crm.service.system.SysUserService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 自定义UserDetailsService实现
 *
 * @author gzy
 */
@Service
public class MyUserDetailService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (org.apache.commons.lang3.StringUtils.isBlank(username)) {
            throw new BizException(BizCodeEnum.LOGIN_PARAMETER_PARSE_ERROR);
        }
        SysUser sysUser = sysUserService.findSysUserByAccountAndDeleteTimeIsNull(username);
        if (ObjectUtils.isEmpty(sysUser)) {
            throw new BizException(BizCodeEnum.USER_LOGIN_ERROR);
        } else if (SysUserStatusEnum.DISABLED.equals(sysUser.getStatus())) {
            throw new BizException(BizCodeEnum.USER_ACCOUNT_FORBIDDEN);
        }
        return new User(sysUser.getAccount(), sysUser.getPassword(), getUserAuthorities(sysUser.getId()));
    }

    /**
     * todo 实现用户拥有权限
     *
     * @param userId userId
     * @return List<GrantedAuthority>
     */
    public List<GrantedAuthority> getUserAuthorities(String userId) {
        String authorities = "admin";
        return AuthorityUtils.createAuthorityList(StringUtils.tokenizeToStringArray(authorities, ","));
    }
}
