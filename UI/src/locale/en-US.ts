import localeMessageBox from '@/components/message-box/locale/en-US';
import localeLogin from '@/views/login/locale/en-US';

import localeWorkplace from '@/views/dashboard/workplace/locale/en-US';

import localeMonitor from '@/views/dashboard/monitor/locale/en-US';

import localeSuccess from '@/views/result/success/locale/en-US';
import localeError from '@/views/result/error/locale/en-US';

import locale403 from '@/views/exception/403/locale/en-US';
import locale404 from '@/views/exception/404/locale/en-US';
import locale500 from '@/views/exception/500/locale/en-US';

import localeSystemCode from '@/views/system/dict/locale/en-US';
import localeSettings from './en-US/settings';

import localeCommonList from './en-US/common';

export default {
  'menu.about': 'About System',
  'menu.schedule.jobs': 'Schedule Jobs',
  'menu.dashboard': 'Dashboard',
  'menu.server.dashboard': 'Dashboard-Server',
  'menu.server.workplace': 'Workplace-Server',
  'menu.server.monitor': 'Monitor-Server',
  'menu.list': 'List',
  'menu.result': 'Result',
  'menu.exception': 'Exception',
  'menu.form': 'Form',
  'menu.profile': 'Profile',
  'menu.visualization': 'Data Visualization',
  'menu.user': 'User Center',
  'menu.arcoWebsite': 'Arco Design',
  'menu.faq': 'FAQ',
  'menu.system': 'System Manage',
  'menu.system.dict': 'Code Manage',
  'menu.system.user': 'User Manage',
  'menu.system.role': 'Role Manage',
  'menu.collect': 'Collect Manage',
  'menu.collect.host': 'Host Manage',
  'menu.collect.template': 'Template Manage',
  'menu.collect.maintenance': 'Maintenance Manage',
  'menu.collect.proxy': 'Proxy Manage',
  'menu.collect.script': 'Script Manage',
  'menu.collect.setting': 'General Setting',
  'navbar.docs': 'Docs',
  'navbar.action.locale': 'Switch to English',
  ...localeSettings,
  ...localeMessageBox,
  ...localeLogin,
  ...localeWorkplace,

  ...localeMonitor,
  ...localeSuccess,
  ...localeError,
  ...locale403,
  ...locale404,
  ...locale500,
  ...localeCommonList,
  ...localeSystemCode,
};
