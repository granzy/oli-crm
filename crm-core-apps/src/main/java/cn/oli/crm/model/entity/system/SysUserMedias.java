package cn.oli.crm.model.entity.system;

import cn.oli.crm.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

/**
 * 用户联系方式媒介表
 *
 * @author gzy
 */
@Getter
@Setter
@Entity
@Table(name = "s_user_medias")
public class SysUserMedias extends BaseEntity {

    @Column(nullable = false)
    @Comment("媒介值:如手机号,钉钉,微信账号")
    private String account;

    @Column(nullable = false)
    @Comment("媒介类型")
    private String type;

    @Column(nullable = false)
    @Comment("排序")
    private Integer sortIndex = 0;

    /**
     * JsonIgnore 防止springmvc返回时, json序列化死循环
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private SysUser sysUser;

}
