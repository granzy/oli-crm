package cn.oli.crm.dao.system;

import cn.oli.crm.dao.GenericRepository;
import cn.oli.crm.model.entity.system.SysDict;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author gzy
 */
@Repository
public interface SysDictRepository extends GenericRepository<SysDict, String> {

    /**
     * 根据字典类型和编码查询字典
     *
     * @param type type
     * @param code code
     * @return SysCode
     */
    SysDict findSysDictByTypeAndCode(String type, String code);

    /**
     * 根据字典类型查询字典
     *
     * @param type type
     * @return List<SysDict>
     */
    List<SysDict> findSysDictsByType(String type);

    //todo 级联删除组织

}