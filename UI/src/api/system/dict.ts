import axios from 'axios';
import { JpaPageResult, Pagination } from '@/types/global';

export interface searchTagFormData {
  queryStr?: string;
  page: Pagination;
}

export interface SystemDict {
  id?: string;
  type?: string;
  name: string;
  code: string;
  description?: string;
  createTime?: string;
  lastModifiedDate?: string;
}

export interface SystemDictListRes extends Partial<JpaPageResult> {
  content: SystemDict[];
}

export function querySystemDictList(params: searchTagFormData) {
  return axios.post<SystemDictListRes>('/sysDict/pageList', params);
}

export function save(params: SystemDict) {
  return axios.post<string>('/sysDict', params);
}

export function deleteDict(id: string) {
  return axios.delete<string>(`/sysDict/${id}`);
}

export function findSysDictByTypeAndCode(type: string, code: string) {
  return axios.post<SystemDict>(`/sysDict/findSysDictByTypeAndCode`, {
    type,
    code,
  });
}

export function findSysDictsByType(type: string) {
  return axios.get<SystemDict[]>(`/sysDict/findSysDictsByType/${type}`);
}
