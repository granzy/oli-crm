package cn.oli.crm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class DatasourceConfig {

    @Autowired
    private DataSourceProperties dataSourceProperties;

    @Autowired
    private JpaProperties jpaProperties;


    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    @Primary
    @Qualifier("appDatasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource appDatasource() {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
        dataSource.setMaximumPoolSize(hikariConfig().getMaximumPoolSize());
        return dataSource;
    }

    @Bean
    @Qualifier("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("appDatasource") DataSource dataSource, EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(dataSource).packages("cn.oli.crm.model.entity").build();
        Properties props = new Properties();
        props.put(Environment.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getName());
        props.put(Environment.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getName());
        entityManagerFactory.setJpaPropertyMap(jpaProperties.getProperties());
        entityManagerFactory.setJpaProperties(props);
        return entityManagerFactory;
    }

    @Bean
    @Qualifier("transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory.getObject()));
    }


}
