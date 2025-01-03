import moment from 'moment';

export function carry(
  value: number,
  units: string[],
  hex: number,
  withUnit = true
) {
  const k = hex;
  const i = Math.floor(Math.log(value) / Math.log(k));
  if (withUnit) {
    return `${Math.round((value / k ** i) * 100) / 100} ${
      units[i < 0 ? 0 : i]
    }`;
  }
  return Math.round((value / k ** i) * 100) / 100;
}

/**
 * 处理uptime单位转换
 * @param seconds
 * @param withUnit
 */
export function uptimeFormatter(seconds: number, withUnit = true) {
  const days = Math.floor(seconds / 86400);
  seconds %= 86400;
  const hours = Math.floor(seconds / 3600);
  seconds %= 3600;
  const minutes = Math.floor(seconds / 60);
  seconds = Math.round(seconds % 60);

  return `${days}${withUnit ? '天' : ''}, ${hours
    .toString()
    .padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds
    .toString()
    .padStart(2, '0')}`;
}

/**
 * 处理ms s单位转换
 * @param value
 * @param unit
 * @param withUnit
 */
export function formatTime(value: number, unit = 'ms', withUnit = true) {
  let totalSeconds;
  function formatNumber(num: number) {
    return num % 1 === 0 ? num.toString() : num.toFixed(2);
  }

  if (unit === 'ms') {
    totalSeconds = value / 1000;
  } else if (unit === 's') {
    totalSeconds = value;
    if (totalSeconds < 1) {
      const milliseconds = formatNumber(totalSeconds * 1000);
      return withUnit ? `${milliseconds}毫秒` : milliseconds;
    }
  } else {
    throw new Error('不支持的单位。只允许使用 "s" 和 "ms"。');
  }

  if (totalSeconds < 60) {
    if (totalSeconds < 0.1) {
      return withUnit ? `${value}毫秒` : value;
    }
    const result = formatNumber(totalSeconds);
    return withUnit ? `${result}秒` : result;
  }
  if (totalSeconds < 3600) {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = formatNumber(totalSeconds % 60);
    return withUnit
      ? `${minutes}分钟${seconds}秒`
      : `${minutes}分钟${seconds}秒`;
  }
  if (totalSeconds < 86400) {
    const hours = Math.floor(totalSeconds / 3600);
    totalSeconds %= 3600;
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = formatNumber(totalSeconds % 60);
    return withUnit
      ? `${hours}小时${minutes}分钟${seconds}秒`
      : `${hours}小时${minutes}分钟${seconds}秒`;
  }
  const days = Math.floor(totalSeconds / 86400);
  totalSeconds %= 86400;
  const hours = Math.floor(totalSeconds / 3600);
  totalSeconds %= 3600;
  const minutes = Math.floor(totalSeconds / 60);
  const seconds = formatNumber(totalSeconds % 60);
  return withUnit
    ? `${days}天${hours}小时${minutes}分钟${seconds}秒`
    : `${days}天${hours}小时${minutes}分钟${seconds}秒`;
}

const getTransUnit = (baseUnit: string) => {
  if (baseUnit === 's') {
    return '秒';
  }
  if (baseUnit === 'ms') {
    return '毫秒';
  }
  return '';
};

export function unitFormatter(
  baseUnit: string,
  value: number,
  withUnit = true
): string | number {
  let result = value || '-';
  // value必须为number类型,在此进行强制转换
  value = Number(value);
  if (value === 0) {
    // 感叹号在zabbix监控项单位中表示不转换
    if (baseUnit.startsWith('!')) {
      return `0${baseUnit.replace('!', ' ')}`;
    }

    return withUnit && !['text', 'uptime', 'unixtime'].includes(baseUnit)
      ? `0 ${getTransUnit(baseUnit)}`
      : '0';
  }
  if (baseUnit !== 'text') {
    result = (value || '-') + (baseUnit || '');
    // 感叹号在zabbix监控项单位中表示不转换
    if (baseUnit.startsWith('!')) {
      result = Math.round(value * 100) / 100 + baseUnit.replace('!', ' ');
    } else {
      switch (baseUnit) {
        case 'bps':
          result = carry(
            value,
            ['bps', 'Kbps', 'Mbps', 'Gbps', 'Tbps', 'Pbps'],
            1000,
            withUnit
          );
          break;
        case 'Bps':
          result = carry(
            value,
            ['Bps', 'KBps', 'MBps', 'GBps', 'TBps', 'PBps'],
            1000,
            withUnit
          );
          break;
        case 'HZ':
          result = carry(
            value,
            ['HZ', 'KHZ', 'MHZ', 'GHZ', 'THZ', 'PHZ'],
            1000,
            withUnit
          );
          break;
        case 'b':
          result = carry(
            value,
            ['b', 'Kb', 'Mb', 'Gb', 'Tb', 'Pb', 'Eb', 'Zb', 'Yb'],
            1000,
            withUnit
          );
          break;
        case 'B':
          result = carry(
            value,
            ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'],
            1024,
            withUnit
          );
          break;
        case 'b/s':
          result = carry(
            value,
            [
              'b/s',
              'Kb/s',
              'Mb/s',
              'Gb/s',
              'Tb/s',
              'Pb/s',
              'Eb/s',
              'Zb/s',
              'Yb/s',
            ],
            1000,
            withUnit
          );
          break;
        case 'B/s':
          result = carry(
            value,
            [
              'B/s',
              'KB/s',
              'MB/s',
              'GB/s',
              'TB/s',
              'PB/s',
              'EB/s',
              'ZB/s',
              'YB/s',
            ],
            1024,
            withUnit
          );
          break;
        case 'ms':
          result = formatTime(value, 'ms', withUnit);
          break;
        case 's':
          result = formatTime(value, 's', withUnit);
          break;
        case 'uptime':
          result = uptimeFormatter(value, withUnit);
          break;
        case 'unixtime':
          result = moment(value * 1000).format('YYYY-MM-DD HH:mm:ss');
          break;
        case '%':
          if (withUnit) {
            result = `${Math.round(Number(value) * 100) / 100} %`;
          } else {
            result = Math.round(Number(value) * 100) / 100;
          }
          break;
        case 'sectors':
          result = carry(
            value,
            ['sectors', 'Ksectors', 'Msectors', 'Gsectors', 'Tsectors'],
            1000,
            withUnit
          );
          break;
        case 'ips':
          result = carry(
            value,
            ['ips', 'Kips', 'Mips', 'Gips', 'Tips'],
            1000,
            withUnit
          );
          break;
        case '':
          result = Math.round(Number(value) * 100) / 100;
          break;
        default:
          result = withUnit
            ? `${Math.round(Number(value) * 100) / 100} ${baseUnit}`
            : value;
      }
    }
  }
  return result;
}

export function formatFixedNumber(num: number, decimals: number) {
  const formattedNumber = num.toFixed(decimals);
  const parts = formattedNumber.split('.');
  if (parts.length === 2) {
    const integerPart = parts[0];
    let decimalPart = parts[1];
    while (decimalPart.endsWith('0')) {
      decimalPart = decimalPart.slice(0, -1);
    }
    if (decimalPart === '') {
      return integerPart;
    }
    return `${integerPart}.${decimalPart}`;
  }
  return formattedNumber;
}
