package cn.oli.crm.exceptions;

/**
 * 未授权异常
 *
 * @author csj
 */
public class PermissionForbiddenException extends BizException {
    public PermissionForbiddenException() {
        super();
    }

    public PermissionForbiddenException(String msg) {
        super(msg);
    }

    public PermissionForbiddenException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public PermissionForbiddenException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public PermissionForbiddenException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
