package cn.oli.crm.exceptions;

/**
 * token校验失败异常
 *
 * @author gzy
 */
public class TokenInvalidateException extends BizException {
    public TokenInvalidateException() {
        super();
    }

    public TokenInvalidateException(String msg) {
        super(msg);
    }

    public TokenInvalidateException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public TokenInvalidateException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public TokenInvalidateException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
