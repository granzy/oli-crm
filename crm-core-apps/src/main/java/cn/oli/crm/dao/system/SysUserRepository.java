package cn.oli.crm.dao.system;

import cn.oli.crm.dao.GenericRepository;
import cn.oli.crm.model.entity.system.SysUser;
import org.springframework.stereotype.Repository;

/**
 * @author gzy
 */
@Repository
public interface SysUserRepository extends GenericRepository<SysUser, String> {

    /**
     * 根据账户获取用户
     *
     * @param account
     * @return
     */
    SysUser findSysUserByAccountAndDeleteTimeIsNull(String account);

}