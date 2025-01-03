package cn.oli.crm.controller.system;

import cn.oli.crm.model.dto.system.SysUserDto;
import cn.oli.crm.model.entity.system.SysUser;
import cn.oli.crm.model.vo.system.SysUserPasswordResetVo;
import cn.oli.crm.model.vo.system.SysUserQueryVo;
import cn.oli.crm.service.system.SysUserService;
import cn.oli.crm.utils.RsaUtil;
import cn.oli.crm.utils.SystemUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * @author gzy
 */
@Validated
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping
    public String save(@Valid @RequestBody SysUser sysUser) {
        return sysUserService.saveOrUpdate(sysUser);
    }

    @DeleteMapping("/{id}")
    public void delete(@Valid @NotNull @PathVariable("id") String id) {
        sysUserService.logicDelete(id);
    }

    @PostMapping("/pageList")
    public Page<SysUserDto> pageList(@RequestBody @Valid SysUserQueryVo query) {
        return sysUserService.pageList(query);
    }

    @GetMapping("/fetchSysUserByIds")
    public List<SysUserDto> fetchSysUserByIds(@RequestParam("ids") List<String> ids) {
        return sysUserService.fetchSysUserByIds(ids);
    }

    @GetMapping("/fetchSysUserByUserContext")
    public SysUserDto fetchSysUserByUserContext() {
        SysUser sysUser = SystemUtil.getCurrentUser();
        if (sysUser != null) {
            SysUserDto userDto = sysUserService.findSysUserDtoByAccount(sysUser.getAccount());
            if (Objects.nonNull(userDto)) {
                return userDto;
            }
        }
        return null;
    }

    @GetMapping("/fetchSysUserByAccount/{account}")
    public SysUserDto fetchSysUserByUserContext(@Valid @NotNull @PathVariable("account") String account) {
        return sysUserService.findSysUserDtoByAccount(account);
    }


    @GetMapping("/fetchSysUserByUserName/{userName}")
    public List<SysUserDto> fetchSysUserByUserName(@Valid @NotNull @PathVariable("userName") String userName) {
        return sysUserService.fetchSysUserByUserName(userName);
    }

    @PutMapping("/resetPassword")
    public void resetPassword(@RequestBody SysUserPasswordResetVo sysUserPasswordResetVo) throws Exception {
        sysUserService.resetPassword(sysUserPasswordResetVo);
    }

    @PostMapping("/validatePassword")
    public Boolean validatePassword(@RequestBody SysUserPasswordResetVo sysUserPasswordResetVo) throws Exception {
        if (StringUtils.isBlank(sysUserPasswordResetVo.getRawPassword())) {
            throw new Exception("原密码不能为空");
        }
        String password = RsaUtil.decryptByPrivateKey(sysUserPasswordResetVo.getRawPassword());
        SysUser user = sysUserService.findById(sysUserPasswordResetVo.getId()).orElse(null);
        return user != null && new BCryptPasswordEncoder().matches(password, user.getPassword());
    }


    @GetMapping(value = "/customLogout")
    public String logout() {
        // 重定向到Spring Security的注销URL，触发注销流程
        return "redirect:/auth/logout";
    }
}
