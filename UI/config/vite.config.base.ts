import { resolve } from 'path';
import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import svgLoader from 'vite-svg-loader';
import { vitePluginForArco } from '@arco-plugins/vite-vue';
// @ts-ignore
import dayjs from 'dayjs';
import configArcoStyleImportPlugin from './plugin/arcoStyleImport';
// @ts-ignore
import pkg from '../package.json';

const { dependencies, devDependencies, name, description, version } = pkg;
const APP_INFO = {
  pkg: { dependencies, devDependencies, name, description, version },
  lastBuildTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
};
export default defineConfig({
  plugins: [
    vue(),
    vueJsx(),
    svgLoader({ svgoConfig: {} }),
    configArcoStyleImportPlugin(),
    vitePluginForArco({
      style: 'css',
      theme: '@arco-themes/vue-umop-x-line',
    }),
  ],
  resolve: {
    alias: [
      {
        find: '@',
        replacement: resolve(__dirname, '../src'),
      },
      {
        find: 'assets',
        replacement: resolve(__dirname, '../src/assets'),
      },
      {
        find: 'vue-i18n',
        replacement: 'vue-i18n/dist/vue-i18n.cjs.js', // Resolve the i18n warning issue
      },
      {
        find: 'vue',
        replacement: 'vue/dist/vue.esm-bundler.js', // compile template
      },
    ],
    extensions: ['.ts', '.js'],
  },
  define: {
    'process.env': {},
    'APP_INFO': JSON.stringify(APP_INFO),
    '__VUE_PROD_HYDRATION_MISMATCH_DETAILS__': true,
  },
  css: {
    preprocessorOptions: {
      less: {
        modifyVars: {
          hack: `true; @import (reference) "${resolve(
            'src/assets/style/hack-var.less'
          )}";`,
        },
        javascriptEnabled: true,
      },
      // scss: {
      //   additionalData: '@import "src/assets/style/vxe-grid.scss";',
      // },
    },
  },
  server: {
    host: '0.0.0.0',
  },
});
