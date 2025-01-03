package cn.oli.crm.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT权限相关配置类
 *
 * @author csj
 */
@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "security.permission")
public class PermConfig {
    private int expire;
    private String[] whiteList;
}
