package cn.oli.crm.utils;

import cn.oli.crm.config.PermConfig;
import cn.oli.crm.constant.Constants;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * JWT工具类
 *
 * @author gzy
 * edit by csj: remove ConfigurationProperties annotation @2023-12-5
 */
@Component
public class JwtUtil {

    @Resource
    private PermConfig permConfig;

    public String generatorToken(Map<String, Object> payload) {
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.SECOND, permConfig.getExpire());
        //签发时间
        payload.put(JWTPayload.ISSUED_AT, now);
        //过期时间
        payload.put(JWTPayload.EXPIRES_AT, newTime);
        //生效时间
        payload.put(JWTPayload.NOT_BEFORE, now);

        return JWTUtil.createToken(payload, Constants.JWT_SECRET.getBytes());
    }

}
