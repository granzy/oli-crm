<template>
  <div class="navbar">
    <div class="left-side">
      <a-space>
        <img alt="logo" style="width: 25px; height: 25px" :src="logo" />
        <a-typography-title
          :style="{ margin: 0, fontSize: '18px' }"
          :heading="5"
        >
          {{ systemName }}
        </a-typography-title>
        <icon-menu-fold
          v-if="!topMenu && appStore.device === 'mobile'"
          style="font-size: 22px; cursor: pointer"
          @click="toggleDrawerMenu"
        />
      </a-space>
    </div>
    <div class="center-side">
      <Menu v-if="topMenu" />
    </div>
    <ul class="right-side">
      <li v-if="reportStore.isDownloading">
        <a-popover>
          <a-button type="text" :shape="'circle'">
            <template #icon>
              <img :src="downloadSvg" alt="" />
            </template>
          </a-button>
          <template #content>
            <a-row
              v-for="(name, i) in reportStore.downloadList"
              :key="i"
              style="width: 320px"
            >
              <a-col>
                <a-spin :size="12" />
                <span style="margin-left: 2px">
                  <icon-file />
                  正在准备文件: {{ name }}
                </span>
              </a-col>
            </a-row>
          </template>
        </a-popover>
      </li>
      <li>
        <a-tooltip
          :content="
            theme === 'light'
              ? $t('settings.navbar.theme.toDark')
              : $t('settings.navbar.theme.toLight')
          "
        >
          <a-button
            class="nav-btn"
            type="outline"
            :shape="'circle'"
            @click="handleToggleTheme"
          >
            <template #icon>
              <icon-moon-fill v-if="theme === 'dark'" />
              <icon-sun-fill v-else />
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <li>
        <a-tooltip
          :content="
            isFullscreen
              ? $t('settings.navbar.screen.toExit')
              : $t('settings.navbar.screen.toFull')
          "
        >
          <a-button
            class="nav-btn"
            type="outline"
            :shape="'circle'"
            @click="toggleFullScreen"
          >
            <template #icon>
              <icon-fullscreen-exit v-if="isFullscreen" />
              <icon-fullscreen v-else />
            </template>
          </a-button>
        </a-tooltip>
      </li>
      <li>
        <a-dropdown trigger="click">
          <a-avatar
            :style="{
              backgroundColor: '#00d0b6',
              marginRight: '8px',
              cursor: 'pointer',
            }"
          >
            {{ username }}
          </a-avatar>
          <template #content>
            <a-doption>
              <a-space @click="resetPasswordVisible = true">
                <icon-lock />
                <span>修改密码</span>
              </a-space>
            </a-doption>
            <a-doption>
              <a-space @click="handleLogout">
                <icon-export />
                <span>
                  {{ $t('messageBox.logout') }}
                </span>
              </a-space>
            </a-doption>
          </template>
        </a-dropdown>
      </li>
    </ul>
    <reset-password
      :visible="resetPasswordVisible"
      @cancel="resetPasswordVisible = false"
      @ok="resetPasswordVisible = false"
    />
  </div>
</template>

<script lang="ts" setup>
  import { ref, computed, inject } from 'vue';
  import { useDark, useToggle, useFullscreen } from '@vueuse/core';
  import { useAppStore, useUserStore, useReportStore } from '@/store';
  import useUser from '@/hooks/user';
  import Menu from '@/components/menu/index.vue';
  import logo from '@/assets/logo.svg?url';
  import downloadSvg from '@/assets/svg/download.svg?url';
  import ResetPassword from '@/views/system/user/password/reset-password.vue';

  const appStore = useAppStore();
  const userStore = useUserStore();
  const reportStore = useReportStore();
  const { logout } = useUser();
  const { isFullscreen, toggle: toggleFullScreen } = useFullscreen();

  const resetPasswordVisible = ref(false);

  const username = computed(() => {
    return userStore.username;
  });

  const theme = computed(() => {
    return appStore.theme;
  });
  const topMenu = computed(() => appStore.topMenu && appStore.menu);
  const isDark = useDark({
    selector: 'body',
    attribute: 'arco-theme',
    valueDark: 'dark',
    valueLight: 'light',
    storageKey: 'arco-theme',
    onChanged(dark: boolean) {
      // overridden default behavior
      appStore.toggleTheme(dark);
    },
  });
  const toggleTheme = useToggle(isDark);
  const handleToggleTheme = () => {
    toggleTheme();
  };

  const handleLogout = () => {
    logout();
  };
  const toggleDrawerMenu = inject('toggleDrawerMenu') as () => void;

  const systemName = ref('OliCrm');
</script>

<style scoped lang="less">
  .navbar {
    display: flex;
    justify-content: space-between;
    height: 100%;
    background-color: var(--color-bg-2);
    border-bottom: 1px solid var(--color-border);
  }

  .left-side {
    display: flex;
    align-items: center;
    padding-left: 20px;
  }

  .center-side {
    flex: 1;
  }

  .right-side {
    display: flex;
    padding-right: 20px;
    list-style: none;
    :deep(.locale-select) {
      border-radius: 20px;
    }
    li {
      display: flex;
      align-items: center;
      padding: 0 10px;
    }

    a {
      color: var(--color-text-1);
      text-decoration: none;
    }
    .nav-btn {
      border-color: rgb(var(--gray-2));
      color: rgb(var(--gray-8));
      font-size: 16px;
    }
    .trigger-btn,
    .ref-btn {
      position: absolute;
      bottom: 14px;
    }
    .trigger-btn {
      margin-left: 14px;
    }
  }
</style>

<style lang="less">
  .message-popover {
    .arco-popover-content {
      margin-top: 0;
    }
  }
</style>
