package cn.oli.crm.filter;

import cn.oli.crm.exceptions.BizException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gzy
 */
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    public LoginFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        // 判断请求方式是否是 post
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        log.info("request.getContentType():{}", request.getContentType());
        // 判断请求内容类型是否是json类型
        if (request.getContentType().equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE)) {
            try {
                // 从json中获取前端需认证的用户名和密码信息
                // 从输入流中读取JSON数据
                BufferedReader reader = request.getReader();
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                // 使用Jackson库解析JSON数据
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
                // 将JSON数据转换成Map
                Map<String, String> userInfo = new HashMap<>();
                jsonNode.fields().forEachRemaining(entry -> userInfo.put(entry.getKey(), entry.getValue().asText()));
                String username = userInfo.get(getUsernameParameter());
                String password = userInfo.get(getPasswordParameter());
                // 封装成一个Authentication对象进行认证
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
                setDetails(request, auth);
                return getAuthenticationManager().authenticate(auth);
            } catch (IOException e) {
                log.info("登录失败", e);
                throw new BizException("登录失败!", e);
            }
        }
        return super.attemptAuthentication(request, response);
    }
}
