import { useUserStore } from '@/store';
import { SUPERADMIN, SUPERADMIN_ROLE_ID } from '@/utils/constant';

export const hasReadOnlyRole = () => {
  const userStore = useUserStore();
  const { userRoleList, account } = userStore;
  // 超管: 1.账户等于superadmin 2.角色ID=2
  if (account !== SUPERADMIN.SUPERADMIN) {
    if (userRoleList && userRoleList.length > 0) {
      const roleIds = userRoleList.map((role) => role.id);
      if (!roleIds.includes(SUPERADMIN_ROLE_ID)) {
        const readOnlyRoles = userRoleList.filter((role) => role.readOnly);
        if (readOnlyRoles.length > 0) {
          return true;
        }
      }
    } else {
      return true;
    }
  }
  return false;
};

export const checkReadOnly = (el: HTMLElement) => {
  const hasReadOnly = hasReadOnlyRole();
  if (hasReadOnly && el.parentNode) {
    el.style.display = 'none';
  }
};

export default {
  mounted(el: HTMLElement) {
    checkReadOnly(el);
  },
  updated(el: HTMLElement) {
    checkReadOnly(el);
  },
};
