package cn.oli.crm.service.system;

import cn.oli.crm.dao.system.SysDictRepository;
import cn.oli.crm.model.entity.system.QSysDict;
import cn.oli.crm.model.entity.system.SysDict;
import cn.oli.crm.model.vo.common.PageVo;
import cn.oli.crm.model.vo.system.SysDictQueryVo;
import cn.oli.crm.service.BaseService;
import cn.oli.crm.utils.StrUtil;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author gzy
 */
@Service
public class SysDictService extends BaseService<SysDict, String> {

    @Resource
    private SysDictRepository sysDictRepository;

    private final QSysDict qSysDict = QSysDict.sysDict;

    @Transactional(readOnly = true)
    public Page<SysDict> pageList(SysDictQueryVo vo) {
        Predicate predicate = buildConditions(vo);
        PageVo page = vo.getPage();
        Pageable pageable = PageRequest.of(page.getPageNo(),
                Optional.ofNullable(page.getPageSize()).orElse(PageVo.DEFAULT_PAGE_SIZE),
                Sort.Direction.DESC,
                Optional.ofNullable(page.getOrderByColumn()).orElse("createTime"));
        return super.genericRepository.findAll(predicate, pageable);
    }

    private Predicate buildConditions(SysDictQueryVo req) {
        BooleanExpression expression = Expressions.asBoolean(true).isTrue();
        String keyword = req.getQueryStr();
        if (StringUtils.isNotBlank(keyword)) {
            expression = expression.and(
                    qSysDict.type.like("%" + StrUtil.sqlSpecialCharactersEscape(keyword) + "%", '!')
                            .or(qSysDict.code.like("%" + StrUtil.sqlSpecialCharactersEscape(keyword) + "%", '!'))
                            .or(qSysDict.name.like("%" + StrUtil.sqlSpecialCharactersEscape(keyword) + "%", '!'))
            );
        }
        return expression;
    }

    @Transactional(readOnly = true)
    public SysDict findSysDictByTypeAndCode(String type, String code) {
        return sysDictRepository.findSysDictByTypeAndCode(type, code);
    }

    @Transactional(readOnly = true)
    public List<SysDict> findSysDictsByType(String type) {
        return sysDictRepository.findSysDictsByType(type);
    }
}
