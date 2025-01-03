package cn.oli.crm.model.entity.system;

import cn.oli.crm.enums.SysUserStatusEnum;
import cn.oli.crm.model.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * @author gzy
 */
@Getter
@Setter
@Entity
@Table(name = "s_user", uniqueConstraints = {@UniqueConstraint(columnNames = {"account", "deleteTime"}, name = "un_s_user_account_delete_time")})
@NamedEntityGraph(name = "SysUser.Graph", attributeNodes = {@NamedAttributeNode("mediasList")})
@DynamicUpdate
public class SysUser extends BaseEntity {

    /**
     * 账户名
     */
    @Comment("账户名")
    private String account;

    /**
     * 密码
     */
    @Comment("密码")
    private String password;

    /**
     * 用户名称
     */
    @Comment("用户名称")
    private String username;

    /**
     * 描述
     */
    @Comment("描述")
    @Size(max = 500)
    private String description;

    /**
     * 状态
     */
    @Column(nullable = false)
    @Comment("状态")
    @Enumerated(EnumType.STRING)
    private SysUserStatusEnum status = SysUserStatusEnum.ENABLED;

    /**
     * 删除时间
     */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = TIME_ZONE)
    private Instant deleteTime;

    @OneToMany(mappedBy = "sysUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortIndex ASC")
    private List<SysUserMedias> mediasList = new LinkedList<>();

}
