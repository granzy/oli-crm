package cn.oli.crm.service.system;

import cn.oli.crm.constant.Constants;
import cn.oli.crm.dao.system.SysUserRepository;
import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.exceptions.BizException;
import cn.oli.crm.model.dto.system.SysUserDto;
import cn.oli.crm.model.entity.system.QSysUser;
import cn.oli.crm.model.entity.system.SysUser;
import cn.oli.crm.model.entity.system.SysUserMedias;
import cn.oli.crm.model.vo.common.PageVo;
import cn.oli.crm.model.vo.system.SysUserPasswordResetVo;
import cn.oli.crm.model.vo.system.SysUserQueryVo;
import cn.oli.crm.service.BaseService;
import cn.oli.crm.utils.RsaUtil;
import cn.oli.crm.utils.StrUtil;
import cn.oli.crm.utils.SystemUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gzy
 */
@Service
@Slf4j
public class SysUserService extends BaseService<SysUser, String> {

    @Resource
    private SysUserRepository sysUserRepository;


    private final QSysUser qSysUser = QSysUser.sysUser;


    @Transactional(rollbackFor = Throwable.class)
    public String saveOrUpdate(SysUser sysUser) {
        for (SysUserMedias medias : sysUser.getMediasList()) {
            medias.setSysUser(sysUser);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        // 更新时password不更新
        if (StringUtils.isNotBlank(sysUser.getId())) {
            Optional<SysUser> optionalSysUser = super.findById(sysUser.getId());
            optionalSysUser.ifPresent(user -> sysUser.setPassword(user.getPassword()));
        }
        return super.save(sysUser).getId();
    }

    private JPAQuery<SysUserDto> getSysUserDtoJPAQuery() {
        return jpaQueryFactory.select(Projections.fields(
                SysUserDto.class,
                qSysUser.id,
                qSysUser.account,
                qSysUser.username,
                qSysUser.description,
                qSysUser.status,
                qSysUser.deleteTime,
                qSysUser.createTime,
                qSysUser.updateTime
        )).from(qSysUser);
    }

    @Transactional(readOnly = true)
    public Page<SysUserDto> pageList(SysUserQueryVo vo) {
        PageVo page = vo.getPage();
        BooleanExpression conditions = (BooleanExpression) this.buildConditions(vo);

        JPAQuery<SysUserDto> jpaQuery = getSysUserDtoJPAQuery();

        // superadmin账户只有superadmin可以查询到
        if (!Constants.SUPER_ADMIN.equals(SystemUtil.getCurrentUser().getAccount())) {
            conditions = conditions.and(qSysUser.account.ne(Constants.SUPER_ADMIN));
        }

        List<SysUserDto> pageList = jpaQuery
                .where(conditions)
                .orderBy(qSysUser.createTime.desc())
                .offset((long) page.getPageNo() * page.getPageSize())
                .limit(page.getPageSize())
                .fetch();

        List<String> userIds = pageList.stream().map(SysUserDto::getId).collect(Collectors.toList());

        JPAQuery<Long> countQuery = jpaQueryFactory.select(qSysUser.id.count()).from(qSysUser);
        Long count = countQuery.where(conditions).fetchOne();
        PageRequest pageRequest = PageRequest.of(page.getPageNo(), page.getPageSize());

        return new PageImpl<>(pageList, pageRequest, count);
    }

    private Predicate buildConditions(SysUserQueryVo req) {
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        String keyword = req.getQueryStr();
        if (StringUtils.isNotBlank(keyword)) {
            expression = expression.and(
                    qSysUser.account.like("%" + StrUtil.sqlSpecialCharactersEscape(keyword) + "%", '!')
                            .or(qSysUser.username.like("%" + StrUtil.sqlSpecialCharactersEscape(keyword) + "%", '!'))
            ).and(qSysUser.deleteTime.isNull());
        }
        // 过滤掉已删除用户
        expression = expression.and(qSysUser.deleteTime.isNull());
        return expression;
    }

    @Transactional(rollbackFor = Throwable.class)
    public void logicDelete(String id) {
        TimeInterval timer = DateUtil.timer();
        SysUser sysUser = super.findById(id).orElse(null);
        if (sysUser != null) {
            sysUser.setDeleteTime(Instant.now());
            super.save(sysUser);
        }
    }

    @Transactional(readOnly = true)
    public List<SysUserDto> fetchSysUserByIds(Collection<String> ids) {
        return getSysUserDtoJPAQuery()
                .where(qSysUser.id.in(ids).and(qSysUser.deleteTime.isNull()))
                .fetch();
    }

    //根据id查询用户并转换为map key为id value为SysUserDto
    @Transactional(readOnly = true)
    public Map<String, SysUserDto> fetchSysUserMapByIds(Collection<String> ids) {
        return getSysUserDtoJPAQuery()
                .where(qSysUser.id.in(ids).and(qSysUser.deleteTime.isNull()))
                .fetch()
                .stream()
                .collect(Collectors.toMap(SysUserDto::getId, sysUserDto -> sysUserDto));
    }

    //根据accounts查询用户并转换为map key为id value为SysUserDto
    @Transactional(readOnly = true)
    public Map<String, SysUserDto> fetchSysUserMapByAccounts(Collection<String> accounts) {
        return getSysUserDtoJPAQuery()
                .where(qSysUser.account.in(accounts).and(qSysUser.deleteTime.isNull()))
                .fetch()
                .stream()
                .collect(Collectors.toMap(SysUserDto::getAccount, sysUserDto -> sysUserDto));
    }

    @Transactional(readOnly = true)
    public SysUser findSysUserByAccountAndDeleteTimeIsNull(String account) {
        return sysUserRepository.findSysUserByAccountAndDeleteTimeIsNull(account);
    }

    @Transactional(readOnly = true)
    public SysUserDto findSysUserDtoByAccount(String account) {
        return getSysUserDtoJPAQuery()
                .where(qSysUser.account.eq(account).and(qSysUser.deleteTime.isNull()))
                .fetchOne();
    }

    @Transactional(readOnly = true)
    public List<SysUserDto> fetchSysUserByUserName(String userName) {
        return getSysUserDtoJPAQuery()
                .where(qSysUser.username.like("%" + userName + "%").and(qSysUser.deleteTime.isNull()))
                .fetch();
    }

    @Transactional(readOnly = true)
    public Long countSysUser() {
        BooleanExpression conditions = Expressions.asBoolean(true).isTrue();
        // superadmin账户只有superadmin可以查询到
        if (!Constants.SUPER_ADMIN.equals(SystemUtil.getCurrentUser().getAccount())) {
            conditions = conditions.and(qSysUser.account.ne(Constants.SUPER_ADMIN));
        }
        conditions = conditions.and(qSysUser.deleteTime.isNull());
        return jpaQueryFactory.select(qSysUser.id.count()).from(qSysUser).where(conditions).fetchOne();
    }

    @Transactional(readOnly = true)
    public boolean isSystemAdmin() {
        SysUser sysUser = super.findById(SystemUtil.getCurrentUser().getId()).orElse(null);
        boolean result = false;
        if (!Objects.isNull(sysUser)) {
            result = Constants.SUPER_ADMIN.equals(sysUser.getAccount()) || Constants.ADMIN.equals(sysUser.getAccount());
        }
        return result;
    }

    public void resetPassword(SysUserPasswordResetVo sysUserPasswordResetVo) {
        // 必要参数检查
        if (StringUtils.isBlank(sysUserPasswordResetVo.getId())
                || StringUtils.isBlank(sysUserPasswordResetVo.getPassword())
                || StringUtils.isBlank(sysUserPasswordResetVo.getRawPassword())
                || StringUtils.isBlank(sysUserPasswordResetVo.getConfirmPassword())) {
            throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "参数错误,缺少必要参数!");
        }
        try {
            String id = sysUserPasswordResetVo.getId();
            SysUser sysUser = super.findById(id).orElse(null);
            if (sysUser != null) {
                String password = RsaUtil.decryptByPrivateKey(sysUserPasswordResetVo.getPassword());
                String rawPassword = RsaUtil.decryptByPrivateKey(sysUserPasswordResetVo.getRawPassword());
                String confirmPassword = RsaUtil.decryptByPrivateKey(sysUserPasswordResetVo.getConfirmPassword());
                String account = SystemUtil.getCurrentUser().getAccount();
                // 上下文校验
                if (!account.equals(sysUser.getAccount())) {
                    log.error("被修改用户account:{}与当前上下文中用户account:{}不匹配", sysUser.getAccount(), account);
                    throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "重置密码失败!");
                }
                // 确认密码校验
                if (!password.equals(confirmPassword)) {
                    log.error("用户:{}重置密码失败,确认密码与新密码不一致!", account);
                    throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "重置密码失败!");
                }
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                // 验证原密码
                if (!passwordEncoder.matches(rawPassword, sysUser.getPassword())) {
                    log.error("用户:{}重置密码失败,原密码验证失败!", account);
                    throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "重置密码失败!");
                }
                sysUser.setPassword(passwordEncoder.encode(password));
                super.save(sysUser);
            } else {
                throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "用户不存在!");
            }
        } catch (Exception e) {
            throw new BizException(BizCodeEnum.SPECIFIED_QUESTIONED_USER_NOT_EXIST, "密码重置失败!");
        }
    }


}
