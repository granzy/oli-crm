/* eslint-disable no-console */
import axios from 'axios';
import type { AxiosRequestConfig, AxiosResponse } from 'axios';
import { Message } from '@arco-design/web-vue';
import { getToken } from '@/utils/auth';
import qs from 'qs';
import { isJsonString } from '@/utils/is';

export interface HttpResponse<T = unknown> {
  status?: number;
  msg: string;
  code: number;
  data: T;
}

if (import.meta.env.VITE_API_BASE_URL) {
  axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;
}

axios.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    // let each request carry token
    // this example using the JWT token
    // Authorization is a custom headers key
    // please modify it according to the actual situation
    const token = getToken();
    if (token) {
      if (!config.headers) {
        config.headers = {};
      }
      config.headers.Authorization = `Bearer ${token}`;
    }

    if (config.method === 'get') {
      config.paramsSerializer = (params) =>
        qs.stringify(params, { allowDots: true, arrayFormat: 'repeat' });
    }
    return config;
  },
  (error) => {
    // do something
    return Promise.reject(error);
  }
);
// add response interceptors
axios.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response.headers['transfer-action'] !== 'download') {
      const res = response.data;
      if (response.request.responseType === 'blob') {
        response.data.text().then((text: string) => {
          if (isJsonString(text)) {
            const data = JSON.parse(text);
            // 业务错误
            if (data.code && data.code !== 1) {
              Message.error({
                id: 'request-error-message',
                content: data.data || data.msg || data.errors,
                resetOnHover: true,
                closable: true,
              });
            }
          }
        });
      }
      // for backend
      if (res.code && res.code !== 1) {
        const messageContent = (res.data as string) || res.msg;
        if (
          res.code === 20002 ||
          res.code === 20008 ||
          (window.location.hash.startsWith('#/login') && res.code === 20009)
        ) {
          // 登录失败或者账户被锁定不提示Message错误,只抛出异常,异常在登录表单页面错误显示区域显示
          return Promise.reject(new Error(`${messageContent}` || 'Error'));
        }
        Message.error({
          id: 'request-error-message',
          content: messageContent,
          resetOnHover: true,
          closable: true,
        });

        return Promise.reject(
          new Error(`${response.config.url}:${res.msg}:${res.data}` || 'Error')
        );
      }
      return res;
    }
    return response;
  },
  (error) => {
    const resp = error.response.data;
    Message.error({
      content: resp.message || '请求失败',
      duration: 5 * 1000,
    });
    const stacktrace = resp.errors.split('\n\t');
    console.groupCollapsed(
      `%c[${resp.timestamp}] 请求URL: ${resp.path}, 发生异常: ${resp.message}`,
      'color: #EC4032'
    );
    console.group(`%c${stacktrace[0]}`, 'color: #EC4032');
    console.error(stacktrace.splice(1).join('\n'));
    console.groupEnd();
    console.groupEnd();

    return Promise.reject(error);
  }
);
