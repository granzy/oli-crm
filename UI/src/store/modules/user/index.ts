import { defineStore } from 'pinia';
import { LoginData } from '@/api/user';
import { setToken, clearToken } from '@/utils/auth';
import { removeRouteListener } from '@/utils/route-listener';
import {
  getUserInfo,
  login as userLogin,
  SystemUser,
  logout as userLogout,
} from '@/api/system/user';
import { SUPERADMIN } from '@/utils/constant';
import useAppStore from '../app';

const useUserStore = defineStore('user', {
  state: (): SystemUser => ({
    id: undefined,
    account: undefined,
    username: undefined,
    status: undefined,
    password: undefined,
    deleteTime: undefined,
    description: undefined,
    role: 'admin',
    userRoleList: [],
    // name: undefined,
    // avatar: undefined,
    // job: undefined,
    // organization: undefined,
    // location: undefined,
    // email: undefined,
    // introduction: undefined,
    // personalWebsite: undefined,
    // jobName: undefined,
    // organizationName: undefined,
    // locationName: undefined,
    // phone: undefined,
    // registrationDate: undefined,
    // accountId: undefined,
    // certification: undefined,
    // role: '',
  }),

  getters: {
    userInfo(state: SystemUser): SystemUser {
      return { ...state };
    },
    isSuperAdmin(state: SystemUser): boolean {
      const { userRoleList, account } = state;
      return !!(
        account === SUPERADMIN.SUPERADMIN ||
        userRoleList?.map((role) => role.id).includes('2')
      );
    },
  },

  actions: {
    switchRoles() {
      return new Promise((resolve) => {
        this.role = this.role === 'user' ? 'admin' : 'user';
        resolve(this.role);
      });
    },
    // Set user's information
    setInfo(partial: Partial<SystemUser>) {
      this.$patch(partial);
    },

    // Reset user's information
    resetInfo() {
      this.$reset();
    },

    // Get user's information
    async info() {
      const res = await getUserInfo();
      this.setInfo(res.data);
    },

    // Login
    async login(loginForm: LoginData) {
      try {
        const res = await userLogin(loginForm);
        setToken(res.data.token);
        await this.info();
      } catch (err) {
        clearToken();
        throw err;
      }
    },
    logoutCallBack() {
      const appStore = useAppStore();
      this.resetInfo();
      clearToken();
      removeRouteListener();
      appStore.clearServerMenu();
    },
    // Logout
    async logout() {
      try {
        await userLogout();
      } finally {
        this.logoutCallBack();
      }
    },
  },
});

export default useUserStore;
