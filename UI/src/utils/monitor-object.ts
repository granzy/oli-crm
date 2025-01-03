export default function getRouterNameByMonitorObjectCategory(category: string) {
  let routeName = '';
  switch (category) {
    case 'network_device':
      routeName = 'MonitorObjectDetailNetworkDevice';
      break;
    case 'os':
      routeName = 'MonitorObjectDetailOs';
      break;
    default:
      break;
  }
  return routeName;
}

/**
 * 获取合法的zabbix主机名
 * 对字符串中除 数字,字母,空格,点号,短中划线,下划线线以外的字符进行转义,转义为 -
 */
export function getLegalZabbixHostStr(name: string) {
  return name.replace(/[^0-9a-zA-Z\s.\-_]/g, '-');
}
