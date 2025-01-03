package cn.oli.crm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Profile;

@SpringBootTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
@EnableCaching
@Profile("test")
class OliCrmApplicationTests {

    @Test
    void contextLoads() {
    }

}
