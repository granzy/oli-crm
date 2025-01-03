package cn.oli.crm.handler;

import cn.oli.crm.model.entity.system.SysUser;
import cn.oli.crm.model.vo.common.ResponseResult;
import cn.oli.crm.service.system.SysUserService;
import cn.oli.crm.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gzy
 */
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> resultData = new HashMap<>();
        // 设置响应编码格式
        response.setContentType("`application/json;charset=utf-8");
        // 获取用户名
        String username = authentication.getName();
        SysUser currentUser = sysUserService.findSysUserByAccountAndDeleteTimeIsNull(username);
        // 生成 jwt
        Map<String, Object> payload = new HashMap<>();
        payload.put("account", username);
        payload.put("username", currentUser.getUsername());
        String jwt = jwtUtil.generatorToken(payload);
        log.info(username + ":token:{}", jwt);

        ServletOutputStream out = response.getOutputStream();

        resultData.put("token", jwt);
        ObjectMapper objectMapper = new ObjectMapper();
        out.write(objectMapper.writeValueAsString(ResponseResult.success(resultData)).getBytes());
        out.close();
    }
}
