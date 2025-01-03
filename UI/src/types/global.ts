import { AxiosResponse } from 'axios';
import { TreeNodeData } from '@arco-design/web-vue';

export interface AnyObject {
  [key: string]: unknown;
}

export interface Options {
  value: unknown;
  label: string;
}

export interface NodeOptions extends Options {
  children?: NodeOptions[];
}

export interface GetParams {
  body: null;
  type: string;
  url: string;
}

export interface PostData {
  body: string;
  type: string;
  url: string;
}

export interface Pagination {
  current: number;
  pageSize: number;
  orderByColumn?: string;
  orderDirection?: string;
  total: number;
}

export interface BaseEntity {
  id?: string;
  createTime?: string;
  updateTime?: string;
  createUser?: string;
  updateUser?: string;
}

export interface JpaPageResult {
  totalElements: number;
  totalPages: number;
  size: number;
}

export interface TreeOptionData extends TreeNodeData {
  id?: string;
  pid?: string;
  showExtra?: boolean;
  data?: any;
  children?: TreeOptionData[];
}

export interface SelectOptionData {
  value: string;
  label: string;
}

export interface GeneralChart {
  xAxis: string[];
  data: Array<{ name: string; value: number[] }>;
}

export function downloadAttach(res: AxiosResponse, attachName?: string) {
  const fileName =
    attachName ||
    res.headers['content-disposition'].replace('attachment; filename=', '');
  let blob;
  if (!res.headers) {
    blob = res;
  } else {
    blob = new Blob([res.data], { type: res.headers['content-type'] });
  }
  // 创建一个a标签触发打开新网页下载
  const downloadElement = document.createElement('a');
  // @ts-ignore
  const href = window.URL.createObjectURL(blob);
  downloadElement.href = href;
  downloadElement.download = decodeURIComponent(fileName); // 下载后文件名
  document.body.appendChild(downloadElement);
  downloadElement.click(); // 点击下载
  document.body.removeChild(downloadElement); // 下载完成移除元素
  window.URL.revokeObjectURL(href); // 释放掉blob对象
}
