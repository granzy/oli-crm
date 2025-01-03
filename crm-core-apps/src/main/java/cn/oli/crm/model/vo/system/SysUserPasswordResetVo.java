package cn.oli.crm.model.vo.system;

import lombok.Getter;
import lombok.Setter;

/**
 * @author gzy
 */
@Getter
@Setter
public class SysUserPasswordResetVo {

    private String id;

    private String password;

    private String rawPassword;

    private String confirmPassword;
}
