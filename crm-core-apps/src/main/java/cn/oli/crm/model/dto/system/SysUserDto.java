package cn.oli.crm.model.dto.system;

import cn.oli.crm.constant.Constants;
import cn.oli.crm.enums.SysUserStatusEnum;
import cn.oli.crm.model.entity.system.SysUserMedias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;


/**
 * @author gzy
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserDto {

    private String id;

    /**
     * 账户名
     */
    private String account;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 描述
     */
    private String description;

    private SysUserStatusEnum status = SysUserStatusEnum.ENABLED;

    /**
     * 联系方式(媒介)
     */
    private List<SysUserMedias> mediasList = new LinkedList<>();


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = Constants.TIME_ZONE)
    private Instant createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = Constants.TIME_ZONE)
    private Instant updateTime;
}
