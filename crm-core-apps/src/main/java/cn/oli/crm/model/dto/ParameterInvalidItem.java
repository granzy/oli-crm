package cn.oli.crm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数非法项
 *
 * @author csj
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParameterInvalidItem {
    /**
     * 无效字段的名称
     */
    private String fieldName;

    /**
     * 错误信息
     */
    private String message;
}
