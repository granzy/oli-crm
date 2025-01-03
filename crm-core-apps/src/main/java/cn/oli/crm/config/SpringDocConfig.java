package cn.oli.crm.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置类
 *
 * @author csj
 */
@Configuration
public class SpringDocConfig {
    private static final String SECURITY_SCHEME_NAME = "Authorization";

    @Bean
    public OpenAPI createOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UMOP-X平台API")
                        .description("UMOP-X平台 RESTful Web Services 后台API")
                        .contact(new Contact().email("granzy88@gmail.com").name("OliCrm"))
                        .version("v2.0.0")
                        .license(new License().name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("OliCrm API文档")
                        .url("http://www.xxx.cn/")
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
