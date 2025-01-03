package cn.oli.crm.exceptions;


/**
 * 账户异常登录锁定异常
 *
 * @author lyl
 */
public class UserAccountLockedException extends BizException {
    public UserAccountLockedException() {
        super();
    }

    public UserAccountLockedException(String message) {
        super(message);
    }

    public UserAccountLockedException(String format, Object... objects) {
        super(format, objects);
    }

    public UserAccountLockedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserAccountLockedException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
