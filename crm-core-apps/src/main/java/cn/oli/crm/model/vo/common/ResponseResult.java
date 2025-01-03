package cn.oli.crm.model.vo.common;

import cn.oli.crm.enums.BizCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 默认REST JSON包装
 *
 * @author csj
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult implements ResponseWrapper {
    // 业务状态码: Biz code
    private Integer code;
    // 提示消息
    private String msg;
    // 消息内容体
    private Object data;

    public static ResponseResult success() {
        ResponseResult result = new ResponseResult();
        result.setResultCode(BizCodeEnum.SUCCESS);
        return result;
    }

    public static ResponseResult success(Object data) {
        ResponseResult result = new ResponseResult();
        result.setResultCode(BizCodeEnum.SUCCESS);
        result.setData(data);
        return result;
    }

    public static ResponseResult failure() {
        ResponseResult result = new ResponseResult();
        result.setResultCode(BizCodeEnum.FAILURE);
        return result;
    }

    public static ResponseResult failure(BizCodeEnum codeEnum) {
        ResponseResult result = new ResponseResult();
        result.setResultCode(codeEnum);
        return result;
    }

    public static ResponseResult failure(BizCodeEnum codeEnum, Object data) {
        ResponseResult result = new ResponseResult();
        result.setResultCode(codeEnum);
        result.setData(data);
        return result;
    }

    private void setResultCode(BizCodeEnum codeEnum) {
        this.code = codeEnum.code();
        this.msg = codeEnum.message();
    }
}
