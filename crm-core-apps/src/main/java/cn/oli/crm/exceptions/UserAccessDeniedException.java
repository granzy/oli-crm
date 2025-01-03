package cn.oli.crm.exceptions;

/**
 * 禁止访问异常
 *
 * @author gzy
 */
public class UserAccessDeniedException extends BizException {
    public UserAccessDeniedException() {
        super();
    }

    public UserAccessDeniedException(String msg) {
        super(msg);
    }

    public UserAccessDeniedException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public UserAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAccessDeniedException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
