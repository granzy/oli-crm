package cn.oli.crm.enums;

/**
 * HTTP Response 统一状态码定义(业务码、业务消息)
 *
 * @author csj
 */
public enum BizCodeEnum {
    /* 通用态码 */
    SUCCESS(1, "成功"),
    FAILURE(0, "失败"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户信息已过期"),
    LOGIN_CREDENTIAL_EXISTED(20006, "凭证已存在"),
    LOGIN_PARAMETER_PARSE_ERROR(20007, "登录参数解析失败"),
    LOGIN_ACCOUNT_LOCKED(20008, "账号已被锁定，请稍后再试"),

    /* 业务错误：30001-39999 */
    SPECIFIED_QUESTIONED_USER_NOT_EXIST(30001, "业务错误"),
    ZABBIX_API_RETURN_ERROR(30002, "ZABBIX API 返回包含error"),
    SNMP_POLL_API_ERROR(30003, "SNMP 接口异常"),

    /* 系统错误：40001-49999 */
    SYSTEM_INNER_ERROR(40001, "系统内部异常，请联系管理员"),
    SYSTEM_FUN_NOT_IMPL(40002, "系统功能尚未完成，尽请等待"),

    /* 数据错误：50001-599999 */
    RESULT_DATA_NONE(50001, "数据未找到"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),

    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),

    /* 权限错误：70001-79999 */
    PERMISSION_NO_ACCESS(70001, "无访问权限"),
    RESOURCE_EXISTED(70002, "资源已存在"),
    RESOURCE_NOT_EXISTED(70003, "资源不存在"),
    TOKEN_INVALIDATE(70004, "token验证失败"),

    /* Zabbix Api接口错误：80001-89999 */
    ZABBIX_API_ERROR_HOST_ALREADY_EXISTS(80001, "Zabbix Api异常：主机名已存在！错误码：【-32602】"),
    ZABBIX_API_ERROR_HOST_DOES_NOT_EXISTS(80002, "Zabbix Api异常：主机不存在！错误码：【-32500】");

    private final Integer code;
    private final String message;

    BizCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(String name) {
        for (BizCodeEnum item : BizCodeEnum.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (BizCodeEnum item : BizCodeEnum.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
