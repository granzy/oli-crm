import moment from 'moment';
import dayjs from 'dayjs';
import { ShortcutType } from '@arco-design/web-vue';

// eslint-disable-next-line import/prefer-default-export
export function humaneDuration(duration: number): string {
  const d = moment.duration(duration, 'milliseconds');
  const days = Math.floor(d.asDays());
  const hours = Math.floor(d.asHours()) - days * 24;
  const minutes = Math.floor(d.asMinutes()) - (days * 24 + hours) * 60;
  return `${days}天 ${hours}小时 ${minutes}分钟`;
}

export function problemDuration(duration: number): string {
  const currentTimestamp = moment().valueOf();
  return humaneDuration(currentTimestamp - duration * 1000);
}
export function convertTimestampToDateString(
  timestamp: number | string,
  format: string
): string {
  if (typeof timestamp === 'string') {
    timestamp = parseInt(timestamp, 10);
  }
  // 如果是10为的时间戳转为13位
  if (timestamp.toString().length === 10) {
    timestamp *= 1000;
  }
  return moment(timestamp).format(format);
}

export function convertTimestampToDateFullString(timestamp: number | string) {
  return convertTimestampToDateString(timestamp, 'YYYY-MM-DD HH:mm:ss');
}

export function getLastHourRange(hours: number) {
  return [dayjs().subtract(hours, 'hour').valueOf(), dayjs().valueOf()];
}

export const rangeShortcuts: ShortcutType[] = [
  {
    label: '最近30分钟',
    value: () => getLastHourRange(0.5),
  },
  {
    label: '最近1小时',
    value: () => getLastHourRange(1),
  },
  // {
  //   label: '最近3小时',
  //   value: () => getLastHourRange(3),
  // },
  {
    label: '最近6小时',
    value: () => getLastHourRange(6),
  },
  {
    label: '最近24小时',
    value: () => getLastHourRange(24),
  },
  {
    label: '今天',
    value: () => [
      dayjs().hour(0).minute(0).second(0).millisecond(0),
      dayjs().valueOf(),
    ],
  },
  {
    label: '昨天',
    value: () => {
      const todayZero = dayjs().hour(0).minute(0).second(0).millisecond(0);
      return [
        todayZero.subtract(1, 'day').valueOf(),
        todayZero.subtract(1, 'millisecond').valueOf(),
      ];
    },
  },
  {
    label: '本周',
    value: () => [
      dayjs().day(0).hour(0).minute(0).second(0).millisecond(0).valueOf(),
      dayjs().valueOf(),
    ],
  },
  {
    label: '上一周',
    value: () => {
      const firstDayOfThisWeek = dayjs()
        .day(1)
        .hour(0)
        .minute(0)
        .second(0)
        .millisecond(0);
      return [
        firstDayOfThisWeek.subtract(1, 'week').valueOf(),
        firstDayOfThisWeek.subtract(1, 'millisecond').valueOf(),
      ];
    },
  },
  {
    label: '本月',
    value: () => [
      dayjs().date(1).hour(0).minute(0).second(0).millisecond(0).valueOf(),
      dayjs().valueOf(),
    ],
  },
  {
    label: '上一月',
    value: () => {
      const firstDayOfThisMonth = dayjs()
        .date(1)
        .hour(0)
        .minute(0)
        .second(0)
        .millisecond(0);
      return [
        firstDayOfThisMonth.subtract(1, 'month').valueOf(),
        firstDayOfThisMonth.subtract(1, 'millisecond').valueOf(),
      ];
    },
  },
  {
    label: '今年',
    value: () => {
      const firstDayOfThisYear = dayjs()
        .month(0)
        .date(1)
        .hour(0)
        .minute(0)
        .second(0)
        .millisecond(0);
      return [firstDayOfThisYear.valueOf(), dayjs().valueOf()];
    },
  },
  {
    label: '去年',
    value: () => {
      const firstDayOfThisYear = dayjs()
        .month(0)
        .date(1)
        .hour(0)
        .minute(0)
        .second(0)
        .millisecond(0);
      return [
        firstDayOfThisYear.subtract(1, 'year').valueOf(),
        firstDayOfThisYear.subtract(1, 'millisecond').valueOf(),
      ];
    },
  },
];
