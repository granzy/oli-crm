package cn.oli.crm.model.entity.system;

import cn.oli.crm.model.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * @author gzy
 */
@Getter
@Setter
@Entity
@Table(name = "s_dict", uniqueConstraints = {@UniqueConstraint(columnNames = {"code", "type"}, name = "un_s_dict_code_type")})
public class SysDict extends BaseEntity {

    /**
     * 字典类型
     */
    @Comment("字典类型")
    private String type;

    /**
     * 名称
     */
    @Comment("名称")
    private String name;

    /**
     * 编码
     */
    @Comment("编码")
    private String code;

    /**
     * 描述
     */
    @Comment("描述")
    private String description;

    /**
     * 是否启用
     */
    @Column(nullable = false)
    @Comment("是否启用")
    private boolean enable = true;

}
