package cn.oli.crm.exceptions;

/**
 * 用户信息失效异常
 *
 * @author gzy
 */
public class UserNotExistException extends BizException {
    public UserNotExistException() {
        super();
    }

    public UserNotExistException(String msg) {
        super(msg);
    }

    public UserNotExistException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public UserNotExistException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNotExistException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
