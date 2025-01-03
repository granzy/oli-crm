import { DEFAULT_LAYOUT } from '../base';
import { AppRouteRecordRaw } from '../types';

const DASHBOARD: AppRouteRecordRaw = {
  path: '/system',
  name: 'System',
  component: DEFAULT_LAYOUT,
  meta: {
    locale: 'menu.system',
    requiresAuth: true,
    icon: 'icon-xitongguanli1',
    order: 3,
  },
  children: [
    {
      path: 'user',
      name: 'SystemUser',
      component: () => import('@/views/system/user/index.vue'),
      meta: {
        locale: 'menu.system.user',
        requiresAuth: true,
        roles: ['*'],
      },
    },
    {
      path: 'dict',
      name: 'SystemDict',
      component: () => import('@/views/system/dict/index.vue'),
      meta: {
        locale: 'menu.system.dict',
        requiresAuth: true,
        roles: ['*'],
      },
    },
  ],
};

export default DASHBOARD;
