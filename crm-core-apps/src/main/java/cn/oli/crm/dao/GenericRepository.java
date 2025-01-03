package cn.oli.crm.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base Repository
 *
 * @param <T>  Entity Type
 * @param <ID> Primary Key
 * @author csj
 */
@NoRepositoryBean
public interface GenericRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>, QuerydslPredicateExecutor<T> {
}
