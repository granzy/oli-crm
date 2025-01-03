package cn.oli.crm.enums;

import cn.oli.crm.exceptions.*;
import org.springframework.http.HttpStatus;

/**
 * @author csj
 */
public enum BizExceptionEnum {
    /**
     * 无效参数
     */
    PARAMETER_INVALID(ParameterInvalidException.class, HttpStatus.BAD_REQUEST, BizCodeEnum.PARAM_IS_INVALID),

    /**
     * 接口方法不允许
     */
    METHOD_NOT_ALLOWED(MethodNotAllowException.class, HttpStatus.METHOD_NOT_ALLOWED, BizCodeEnum.INTERFACE_ADDRESS_INVALID),

    /**
     * 用户未登录
     */
    UNAUTHORIZED(UserNotLoginException.class, HttpStatus.UNAUTHORIZED, BizCodeEnum.USER_NOT_LOGGED_IN),

    TOKEN_INVALIDATE(TokenInvalidateException.class, HttpStatus.OK, BizCodeEnum.TOKEN_INVALIDATE),

    /**
     * 无访问权限
     */
    FORBIDDEN(PermissionForbiddenException.class, HttpStatus.FORBIDDEN, BizCodeEnum.PERMISSION_NO_ACCESS),

    /**
     * 系统内部错误
     */
    INTERNAL_SERVER_ERROR(InternalServerException.class, HttpStatus.INTERNAL_SERVER_ERROR, BizCodeEnum.SYSTEM_INNER_ERROR);

    private final Class<? extends BizException> eClass;

    private final HttpStatus httpStatus;

    private final BizCodeEnum bizCode;

    BizExceptionEnum(Class<? extends BizException> eClass, HttpStatus httpStatus, BizCodeEnum bizCode) {
        this.eClass = eClass;
        this.httpStatus = httpStatus;
        this.bizCode = bizCode;
    }

    public static boolean isSupportHttpStatus(int httpStatus) {
        for (BizExceptionEnum exceptionEnum : BizExceptionEnum.values()) {
            if (exceptionEnum.httpStatus.value() == httpStatus) {
                return true;
            }
        }

        return false;
    }

    public static boolean isSupportException(Class<?> z) {
        for (BizExceptionEnum exceptionEnum : BizExceptionEnum.values()) {
            if (exceptionEnum.eClass.equals(z)) {
                return true;
            }
        }

        return false;
    }

    public static BizExceptionEnum getByHttpStatus(HttpStatus httpStatus) {
        if (httpStatus == null) {
            return null;
        }

        for (BizExceptionEnum exceptionEnum : BizExceptionEnum.values()) {
            if (httpStatus.equals(exceptionEnum.httpStatus)) {
                return exceptionEnum;
            }
        }

        return null;
    }

    public static BizExceptionEnum getByEClass(Class<? extends BizException> eClass) {
        if (eClass == null) {
            return null;
        }

        for (BizExceptionEnum exceptionEnum : BizExceptionEnum.values()) {
            if (eClass.equals(exceptionEnum.eClass)) {
                return exceptionEnum;
            }
        }

        return null;
    }

    public Class<? extends BizException> getEClass() {
        return eClass;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public BizCodeEnum getBizCode() {
        return bizCode;
    }

}
