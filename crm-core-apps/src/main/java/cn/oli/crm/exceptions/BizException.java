package cn.oli.crm.exceptions;

import cn.oli.crm.enums.BizCodeEnum;
import cn.oli.crm.enums.BizExceptionEnum;
import cn.oli.crm.utils.StrUtil;
import lombok.Getter;

/**
 * 自定义业务异常基础类
 *
 * @author csj
 */
@Getter
public class BizException extends RuntimeException {
    protected String code;

    protected String message;

    protected BizCodeEnum resultCode;

    protected Object data;

    public BizException() {
        BizExceptionEnum exceptionEnum = BizExceptionEnum.getByEClass(this.getClass());
        if (exceptionEnum != null) {
            resultCode = exceptionEnum.getBizCode();
            code = exceptionEnum.getBizCode().code().toString();
            message = exceptionEnum.getBizCode().message();
        }

    }

    public BizException(String message) {
        this();
        this.message = message;
    }

    public BizException(String format, Object... objects) {
        this();
        this.message = StrUtil.formatIfArgs(format, "{}", objects);
    }

    public BizException(BizCodeEnum resultCode, String message, Object data) {
        this(resultCode);
        this.message = message;
        this.data = data;
    }

    public BizException(BizCodeEnum resultCode, Object data) {
        this(resultCode);
        this.data = data;
    }

    public BizException(BizCodeEnum resultCode) {
        this.resultCode = resultCode;
        this.code = resultCode.code().toString();
        this.message = resultCode.message();
    }
}
