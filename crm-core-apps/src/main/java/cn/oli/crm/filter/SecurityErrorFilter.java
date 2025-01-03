package cn.oli.crm.filter;

import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.exceptions.TokenInvalidateException;
import cn.oli.crm.exceptions.UserAccessDeniedException;
import cn.oli.crm.exceptions.UserAccountLockedException;
import cn.oli.crm.exceptions.UserNotExistException;
import cn.oli.crm.model.vo.common.ResponseResult;
import cn.oli.crm.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Spring Security异常统一处理(ControlAdvice无法处理部分)
 *
 * @author csj
 */
@Component
@Slf4j
public class SecurityErrorFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            BizCodeEnum bizCodeEnum;
            ResponseResult result;
            log.error("发生异常", e);
            // 根据异常类型输出不同信息
            if (e instanceof TokenInvalidateException) {
                bizCodeEnum = BizCodeEnum.TOKEN_INVALIDATE;
            } else if (e instanceof UserAccessDeniedException) {
                bizCodeEnum = BizCodeEnum.USER_ACCOUNT_FORBIDDEN;
            } else if (e instanceof UserNotExistException) {
                bizCodeEnum = BizCodeEnum.USER_NOT_EXIST;
            } else if (e instanceof UserAccountLockedException) {
                bizCodeEnum = BizCodeEnum.LOGIN_ACCOUNT_LOCKED;
            } else {
                bizCodeEnum = BizCodeEnum.PERMISSION_NO_ACCESS;
            }
            result = ResponseResult.failure(bizCodeEnum, "");
            response.getWriter().write(JsonUtil.object2Json(result));
        }
    }
}
