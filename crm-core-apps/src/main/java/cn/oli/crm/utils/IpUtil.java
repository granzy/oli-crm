package cn.oli.crm.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * IP 工具
 *
 * <p>
 * 获取IP地址，可以穿透代理
 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
 * </p>
 *
 * @author lyl
 * copyed by umop2 designed by gzy
 */
@Slf4j
public class IpUtil {

    /**
     * 定义HTTP请求头字段数组
     */
    private static final String[] HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IPr",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

    private static final String IPV4_LOCAL_ADDRESS = "127.0.0.1";
    private static final String IPV6_LOCAL_ADDRESS = "0.0.0.0.0.0.0.1";
    private static final String IP_SEPARATOR = ",";
    private static final String IP_UNKNOWN = "unknown";

    public static String getRealIp(HttpServletRequest request) {
        String ipAddr = null;
        try {
            // 判断对象是否为空，空返回空字符串
            if (Objects.equals(request, null)) {
                return "";
            }

            // 循环请求头获取ip
            for (String header : HEADERS) {
                ipAddr = request.getHeader(header);
                if (isNotEmptyIp(ipAddr)) {
                    // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                    if (ipAddr.contains(",")) {
                        ipAddr = ipAddr.split(",")[0];
                    }
                    break;
                }
            }

            // 判断为空时获取本机ip
            if (isEmptyIp(ipAddr)) {
                ipAddr = request.getRemoteAddr();
                if (IPV4_LOCAL_ADDRESS.equals(ipAddr) || IPV6_LOCAL_ADDRESS.equals(ipAddr)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inetAddr = null;
                    try {
                        inetAddr = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("InetAddress.getLocalHost-error, {}", e.getMessage());
                    }
                    assert inetAddr != null;
                    ipAddr = inetAddr.getHostAddress();
                }

                //对于获取到多IP的情况，需找到公网IP
                if (!StringUtils.isBlank(ipAddr) && !ipAddr.contains(IP_UNKNOWN) && ipAddr.contains(IP_SEPARATOR)) {
                    String ipRes = null;
                    String[] ips = ipAddr.split(",");
                    for (String ipz : ips) {
                        if (!isInnerIp(ipz.trim())) {
                            ipRes = ipz.trim();
                            break;
                        }
                    }

                    // 如果未找到公网IP，取第一个内网IP
                    if (Objects.equals(null, ipRes)) {
                        ipRes = ips[0].trim();
                    }
                    ipAddr = ipRes;
                }
            }
        } catch (Exception e) {
            log.error(" IPUtil ERROR, {}", e.getMessage());
        }
        return ipAddr;
    }

    /**
     * 判断ip是否为空，空返回true
     *
     * @param ip ip地址
     * @return 布尔值
     */
    public static boolean isEmptyIp(final String ip) {
        return (ip == null || ip.isEmpty() || ip.trim().isEmpty() || IP_UNKNOWN.equalsIgnoreCase(ip));
    }

    /**
     * 判断ip是否不为空，不为空返回true
     *
     * @param ip ip地址
     * @return 布尔值
     */
    public static boolean isNotEmptyIp(final String ip) {
        return !isEmptyIp(ip);
    }

    /**
     * 获取数值型IP
     *
     * @param ipAddress 字符串IP
     * @return 转化成数值型的IP
     */
    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    /**
     * 判断IP是否是内网地址
     * 私有IP：
     * A类 10.0.0.0-10.255.255.255
     * B类 172.16.0.0-172.31.255.255
     * C类 192.168.0.0-192.168.255.255
     * 127这个网段是环回地址
     *
     * @param ipAddress ip地址
     * @return 布尔值，是否是内网地址
     */
    private static boolean isInnerIp(String ipAddress) {
        long ipNum = getIpNum(ipAddress);

        // 私有IP A类
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");

        // 私有IP B类
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");

        // 私有IP C类
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        return isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || "127.0.0.1".equals(ipAddress);
    }

    /**
     * 是否是内网IP
     *
     * @param userIp IP地址
     * @param begin  开始网段
     * @param end    结束网段
     * @return 布尔值
     */
    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }
}
