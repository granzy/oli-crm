package cn.oli.crm.model.vo.system;

import cn.oli.crm.model.vo.common.PageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author gzy
 */
@Getter
@Setter
public class SysUserQueryVo {


    private PageVo page;

    /**
     * 通用查询条件
     */
    private String queryStr;

    private List<String> orgGroupIds;

    private String userRoleId;

}
