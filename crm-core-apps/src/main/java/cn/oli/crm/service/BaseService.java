package cn.oli.crm.service;

import cn.oli.crm.dao.GenericRepository;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * <pre>
 * 在开发中我们需要在只发出查询语句的方法上添加@Transactional(readOnly = true)注解，将之申明为只读事务。
 *   1. 多条查询下要使用该注解，能够防止多次查询到的数据不一致（维持可重复读）。而且有一定的优化，比如两次相同查询只发出一条sql；
 *   2. 尽管在单条查询下不会出现数据不一致现象，但是使用@Transactional(readOnly = true)注解能够优化查询，源码中提到readOnly=true也存在着可能的优化,
 *   readOnly是否能到来优化还是要看数据库类型和驱动类型
 *
 * Spring Doc:
 *   Read-only status: A read-only transaction can be used when your code reads but does not modify data. Read-only transactions can be a useful
 * optimization in some cases, such as when you are using Hibernate.
 * </pre>
 *
 * @param <T>
 * @param <ID>
 */
public abstract class BaseService<T, ID extends Serializable> {
    @Autowired
    protected GenericRepository<T, ID> genericRepository;

    @Autowired
    protected JPAQueryFactory jpaQueryFactory;

    @Autowired
    protected BlazeJPAQueryFactory blazeJPAQueryFactory;

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll() {
        return this.genericRepository.findAll();
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAll(Sort sort) {
        return this.genericRepository.findAll(sort);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public List<T> findAllById(Iterable<ID> ids) {
        return this.genericRepository.findAllById(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> S save(S entity) {
        return this.genericRepository.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> S saveAndFlush(S entity) {
        return this.genericRepository.saveAndFlush(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return this.genericRepository.saveAll(entities);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <S extends T> List<S> saveAllAndFlush(Iterable<S> entities) {
        return this.genericRepository.saveAllAndFlush(entities);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public Optional<T> findById(ID id) {
        return this.genericRepository.findById(id);
    }

    /**
     * 物理删除
     *
     * @param id
     */
    @Transactional(rollbackFor = Throwable.class)
    public void realDeleteById(ID id) {
        this.genericRepository.deleteById(id);

    }

    /**
     * 物理删除
     *
     * @param ids
     */
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public void realDeleteAllById(Iterable<? extends ID> ids) {
        this.genericRepository.deleteAllById(ids);
    }

    /**
     * @deprecated 仅供测试用例使用
     */
    @Transactional(rollbackFor = Throwable.class)
    @Deprecated
    public void realDeleteAll() {
        this.genericRepository.deleteAll();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void realDeleteAllInBatch() {
        this.genericRepository.deleteAllInBatch();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteAllByIdInBatch(Iterable<ID> ids) {
        this.genericRepository.deleteAllByIdInBatch(ids);
    }


    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public boolean existsById(ID id) {
        return this.genericRepository.existsById(id);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public boolean exists(Predicate predicate) {
        return this.genericRepository.exists(predicate);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void flush() {
        this.genericRepository.flush();
    }

    // 自定义的2个方法，用于在事务中获取到JPAQueryFactory，执行自定义的查询逻辑
    @Transactional(rollbackFor = Throwable.class)
    public <R> R apply(Function<JPAQueryFactory, R> function) {
        return function.apply(this.jpaQueryFactory);
    }

    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    public <R> R applyReadOnly(Function<JPAQueryFactory, R> function) {
        return function.apply(this.jpaQueryFactory);
    }
}
