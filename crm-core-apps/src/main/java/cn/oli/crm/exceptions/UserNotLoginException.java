package cn.oli.crm.exceptions;

/**
 * 尚未认证异常
 *
 * @author csj
 */
public class UserNotLoginException extends BizException {
    public UserNotLoginException() {
        super();
    }

    public UserNotLoginException(String msg) {
        super(msg);
    }

    public UserNotLoginException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public UserNotLoginException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotLoginException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
