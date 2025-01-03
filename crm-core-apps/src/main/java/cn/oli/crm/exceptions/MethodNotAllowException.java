package cn.oli.crm.exceptions;

/**
 * 接口方法拒绝异常
 *
 * @author csj
 */
public class MethodNotAllowException extends BizException {
    public MethodNotAllowException() {
        super();
    }

    public MethodNotAllowException(String msg) {
        super(msg);
    }

    public MethodNotAllowException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public MethodNotAllowException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MethodNotAllowException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
