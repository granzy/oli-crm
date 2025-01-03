package cn.oli.crm.utils;

import cn.oli.crm.model.entity.system.SysUser;
import org.springframework.security.core.context.SecurityContextHolder;

public class SystemUtil {
    public static final String TIME_ZONE = "GMT+8";

    public static SysUser getCurrentUser() {
        SysUser sysUser = null;
        if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null) {
            sysUser = (SysUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }else{
            sysUser = new SysUser();
            sysUser.setId("-1");
            sysUser.setUsername("SYSTEM");
        }
        return sysUser;
    }
}
