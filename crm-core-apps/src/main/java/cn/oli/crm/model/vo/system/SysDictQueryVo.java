package cn.oli.crm.model.vo.system;

import cn.oli.crm.model.vo.common.PageVo;
import lombok.Getter;
import lombok.Setter;

/**
 * @author gzy
 */
@Getter
@Setter
public class SysDictQueryVo {

    private PageVo page;

    private String type;

    private String code;

    /**
     * 通用查询条件
     */
    private String queryStr;

}
