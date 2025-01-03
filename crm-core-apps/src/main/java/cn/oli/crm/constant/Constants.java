package cn.oli.crm.constant;

/**
 * @author SJK
 * @updateTime 2023/9/22
 */
public class Constants {
    public static final String SUPER_ADMIN = "superadmin";
    public static final String ADMIN = "admin";
    public static final String SUPER_ADMIN_ROLE_ID = "2";
    public static final String ADMIN_ROLE_ID = "3";
    public static final String COMMON_USER_ROLE_ID = "4";

    public static final String ROOT_MENU_NAME = "0";
    public static final String SYSTEM_MANAGE_MENU_NAME = "System";

    public static final String JWT_SECRET = "20adf8b1-80ab-4f01-aa67-ddce311cb20c";

    public static final String SEPARATOR = "/";
    public static final String SEPARATOR_UNDERLINE = "_";

    public static final String SYS_CONFIG_GROUP_CODE_MONITOR_OBJECT = "监控对象配置";
    public static final String SYS_CONFIG_CODE_MONITOR_OBJECT_QUERY_CATEGORY_SUBTYPE_BLACKLIST =
            "query_category_subtype_blacklist";
    public static final String SYS_CONFIG_GROUP_CODE_GRAFANA = "grafana";
    public static final String SYS_CONFIG_CODE_GRAFANA_URL = "url";
    public static final String SYS_CONFIG_CODE_GRAFANA_TOKEN = "token";
    public static final String SYS_CONFIG_CODE_GRAFANA_LOKI = "lokiInterfaceConfig";

    public static final String TOPOLOGY_SEPARATOR = "@hong_shi@";

    public static final String HOSTNAME_OID = "1.3.6.1.2.1.1.5.0";
    public static final String SYS_OBJECT_ID_OID = "1.3.6.1.2.1.1.2.0";
    public static final String SYS_DESC_OID = "1.3.6.1.2.1.1.1.0";
    public static final String UNGROUP_NAME = "未分组";

    public static final String NETWORK_IMPORT_TAG_CONTENT_SEPARATOR = ",";

    // 首页实时告警显示的专线标签值
    public static final String INDEX_REAL_TIME_ALARM_LINE_TYPE_TAG = "链路状态";
    public static final int MAX_LOGIN_RETRY_TIMES = 5;

    public static final String TIME_ZONE = "GMT+8";
}


