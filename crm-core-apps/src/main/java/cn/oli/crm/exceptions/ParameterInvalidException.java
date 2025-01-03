package cn.oli.crm.exceptions;

/**
 * 非法参数异常
 *
 * @author csj
 */
public class ParameterInvalidException extends BizException {
    public ParameterInvalidException() {
        super();
    }

    public ParameterInvalidException(String msg) {
        super(msg);
    }

    public ParameterInvalidException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public ParameterInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ParameterInvalidException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
