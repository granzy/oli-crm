<template>
  <div class="page-container">
    <div class="page-panel">
      <a-affix :offset-top="120">
        <a-button
          class="license-btn"
          type="text"
          size="small"
          @click="showLicenseInput(installNewLicense)"
        >
          <template #icon>
            <icon-pen-fill />
          </template>
          更改授权
        </a-button>
      </a-affix>
      <a-space direction="vertical" size="medium" fill>
        <a-typography :style="{ marginTop: '-20px' }">
          <a-typography-title :heading="5">关于</a-typography-title>
          <a-typography-paragraph>
            {{ pkg.description }}
          </a-typography-paragraph>
        </a-typography>
        <a-descriptions :data="buildInfoData" title="系统信息" bordered />
        <!--<a-descriptions-->
        <!--  :data="dependenciesData"-->
        <!--  title="生产环境依赖"-->
        <!--  bordered-->
        <!--/>-->
        <!--<a-descriptions-->
        <!--  :data="devDependenciesData"-->
        <!--  title="开发环境依赖"-->
        <!--  bordered-->
        <!--/>-->
      </a-space>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { onMounted, ref } from 'vue';
  import { Notification } from '@arco-design/web-vue';
  import {
    licenseInput,
    showLicenseInput,
    installLicense,
    getLicense,
  } from '@/api/system/sys-license';
  // eslint-disable-next-line no-undef
  const { lastBuildTime, pkg } = APP_INFO;

  const buildInfoData = ref([
    { label: '版本', value: pkg.version },
    { label: '编译时间', value: lastBuildTime },
  ]);
  const updateLicenseInfo = async () => {
    const { data: lic } = await getLicense();
    buildInfoData.value = [
      { label: '授权类型', value: lic.model === 'trial' ? '试用版' : '正式版' },
      { label: '版本', value: pkg.version },
      { label: '编译时间', value: lastBuildTime },
      { label: '使用期限', value: `${lic.startFrom}~${lic.endOf}` },
    ];
  };
  const installNewLicense = async () => {
    const lic: string = licenseInput.value.computedValue;
    if (lic && lic.trim()) {
      const { data: resp } = await installLicense({
        licenseContent: lic.trim(),
      });
      if (resp.installed) {
        Notification.success({ title: '更改授权', content: '已成功更新授权' });
        await updateLicenseInfo();
      } else {
        Notification.error({
          title: '更改授权',
          content: '更新失败, 授权码已过期!',
        });
      }
    }
  };

  // const dependenciesData = ref(
  //   Object.keys(pkg.dependencies).map((k) => {
  //     return { label: k, value: pkg.dependencies[k] };
  //   })
  // );
  //
  // const devDependenciesData = ref(
  //   Object.keys(pkg.devDependencies).map((k) => {
  //     return { label: k, value: pkg.devDependencies[k] };
  //   })
  // );
  onMounted(async () => {
    await updateLicenseInfo();
  });
</script>

<script lang="ts">
  export default {
    name: 'About',
  };
</script>

<style scoped lang="less">
  .license-btn {
    float: right;
    color: #19a15f;
  }
  .license-btn:hover {
    background-color: var(--color-fill-1);
    color: #19a15f;
  }
</style>
