package cn.oli.crm.utils;

import javax.servlet.http.HttpServletRequest;

public class NetUtil {


    /**
     * 获取请求的真实IP地址
     *
     * @param request HttpServletRequest对象
     * @return 真实IP地址
     */
    public static String getRealIpAddress(HttpServletRequest request) {
        String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP"};
        String ip = "";
        for (String header : headers) {
            ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                break;
            }
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
