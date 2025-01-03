package cn.oli.crm.exceptions;

/**
 * 服务器内部异常
 *
 * @author csj
 */
public class InternalServerException extends BizException {
    public InternalServerException() {
        super();
    }

    public InternalServerException(String msg) {
        super(msg);
    }

    public InternalServerException(String formatMsg, Object... objects) {
        super(formatMsg, objects);
    }

    public InternalServerException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public InternalServerException(String msg, Throwable cause, Object... objects) {
        super(msg, cause, objects);
    }
}
