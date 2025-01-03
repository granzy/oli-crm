import { App } from 'vue';
import readOnly from '@/directive/read-only/index';
import permission from './permission';

export default {
  install(Vue: App) {
    Vue.directive('readOnly', readOnly);
    Vue.directive('permission', permission);
  },
};
