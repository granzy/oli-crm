package cn.oli.crm.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Configuration
public class BlazeQueryDslConfig {

    @Bean
    public BlazeJPAQueryFactory blazeJPAQueryFactory(EntityManager entityManager, CriteriaBuilderFactory criteriaBuilderFactory) {
        return new BlazeJPAQueryFactory(entityManager,criteriaBuilderFactory);
    }

    @Bean
    public CriteriaBuilderFactory criteriaBuilderFactory(EntityManagerFactory entityManagerFactory) {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }
}
