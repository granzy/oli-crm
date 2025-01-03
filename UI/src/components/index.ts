import { App } from 'vue';
import { use } from 'echarts/core';
import { CanvasRenderer } from 'echarts/renderers';
import { BarChart, LineChart, PieChart, RadarChart } from 'echarts/charts';
import {
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
} from 'echarts/components';
import SvgIcon from '@/components/svg-icon/index.vue';
import AutoRefresh from '@/components/auto-refresh/index.vue';
import { Icon } from '@arco-design/web-vue';
import iconFont from '@/assets/icon-font/iconfont?url';
import Chart from './chart/index.vue';
import Breadcrumb from './breadcrumb/index.vue';

// Manually introduce ECharts modules to reduce packing size

use([
  CanvasRenderer,
  BarChart,
  LineChart,
  PieChart,
  RadarChart,
  GridComponent,
  TooltipComponent,
  LegendComponent,
  DataZoomComponent,
  GraphicComponent,
]);

// 注册全局图标组件
const IconFont = Icon.addFromIconFontCn({
  src: iconFont,
});

export default {
  install(Vue: App) {
    Vue.component('Chart', Chart);
    Vue.component('Breadcrumb', Breadcrumb);
    Vue.component('SvgIcon', SvgIcon);
    Vue.component('AutoRefresh', AutoRefresh);
    Vue.component('IconFont', IconFont);
  },
};
