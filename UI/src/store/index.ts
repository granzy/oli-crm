import { createPinia } from 'pinia';
import useAppStore from './modules/app';
import useUserStore from './modules/user';
import useTabBarStore from './modules/tab-bar';
import useReportStore from './modules/report';

const pinia = createPinia();

export { useAppStore, useUserStore, useTabBarStore, useReportStore };
export default pinia;
