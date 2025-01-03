import axios from 'axios';
import { BaseEntity, JpaPageResult, Pagination } from '@/types/global';

export interface LoginData {
  username: string;
  password: string;
}

export interface LoginRes {
  token: string;
}

export interface searchFormData {
  queryStr?: string;
  orgGroupIds?: string[];
  userRoleId?: string;
  page?: Pagination;
}

export interface SystemUserMedias {
  id?: string;
  account: string;
  hiddenAccount?: string;
  type: string;
  sortIndex: number;
}

export interface SystemOrgGroupIdMapping {
  id: string;
}

export interface SystemUser extends BaseEntity {
  id?: string;
  account?: string;
  username?: string;
  status?: string | boolean;
  password?: string;
  confirmPassword?: string;
  deleteTime?: string;
  description?: string;
  mediasList?: SystemUserMedias[];
  phoneList?: SystemUserMedias[];
  mailList?: SystemUserMedias[];
  wechatList?: SystemUserMedias[];
  dingdingList?: SystemUserMedias[];
}

export interface systemUserPageListRes extends Partial<JpaPageResult> {
  content: SystemUser[];
}

export interface PasswordResetByEmail {
  userAccount?: string;
  selectedMediaId?: string;
  mailAddress?: string;
  result?: string;
  sysUserMedias?: SystemUserMedias[];
}

export function querySystemUserList(params: searchFormData) {
  return axios.post<systemUserPageListRes>('/sysUser/pageList', params);
}

export function save(params: SystemUser) {
  return axios.post<string>('/sysUser', params);
}

export function fetchSysUserByIds(ids: string[]) {
  return axios.get<SystemUser[]>('/sysUser/fetchSysUserByIds', {
    params: {
      ids,
    },
  });
}

export function deleteSystemUser(id: string) {
  return axios.delete<string>(`/sysUser/${id}`);
}

export function login(data: LoginData) {
  return axios.post<LoginRes>('/auth/login', data);
}

export function logout() {
  // return axios.get('/auth/logout');
  return axios.get('/sysUser/customLogout');
}

export function getUserInfo() {
  return axios.get<SystemUser>('/sysUser/fetchSysUserByUserContext', {});
}

export function fetchSysUserByAccount(account: string) {
  return axios.get<SystemUser>(`/sysUser/fetchSysUserByAccount/${account}`, {});
}

export interface SysUserAdjustOrgGroupParam {
  userIds: string[];
  orgGroupIds: string[];
}

export function adjustUserOrgGroup(params: SysUserAdjustOrgGroupParam) {
  return axios.post('/sysUser/adjustUserOrgGroup', params);
}

export function fetchSysUserByUserName(username: string) {
  return axios.get<SystemUser[]>(
    `/sysUser/fetchSysUserByUserName/${username}`,
    {}
  );
}

export interface SysUserPasswordResetVo {
  id?: string;
  password?: string;
  rawPassword?: string;
  confirmPassword?: string;
}

export function resetPassword(params: SysUserPasswordResetVo) {
  return axios.put('/sysUser/resetPassword', params);
}

// validatePassword
export function validatePassword(params: SysUserPasswordResetVo) {
  return axios.post<boolean>('/sysUser/validatePassword', params);
}

export function resetPasswordByEmail(param: PasswordResetByEmail) {
  return axios.put<PasswordResetByEmail>(
    '/sysUser/resetPasswordByEmail',
    param
  );
}
