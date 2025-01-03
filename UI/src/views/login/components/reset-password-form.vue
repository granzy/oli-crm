<template>
  <a-modal
    title-align="start"
    title="重置密码"
    :visible="visible"
    :top="'20vh'"
    :align-center="false"
    :mask-closable="false"
    draggable
    unmount-on-close
    @before-open="currentStep = 'inputAccount'"
    @cancel="onCancel"
  >
    <div v-if="currentStep === 'inputAccount'">
      <a-form ref="loginForm" :model="userInfo" layout="vertical">
        <a-form-item
          field="account"
          :rules="[
            { required: true, message: $t('login.form.userName.errMsg') },
          ]"
          :validate-trigger="['change', 'blur']"
          hide-label
        >
          <a-input
            v-model="userInfo.account"
            :placeholder="$t('login.form.userName.placeholder')"
          >
            <template #prefix>
              <icon-user />
            </template>
          </a-input>
        </a-form-item>
      </a-form>
    </div>
    <div v-else-if="currentStep === 'selectEmail'">
      由于用户配置多个邮箱地址请选择一个邮箱地址
      <a-form ref="selectEmailForm" :model="userInfo" layout="vertical">
        <a-form-item
          field="selectedMediaId"
          :rules="[{ required: true, message: '请选择一个邮箱地址' }]"
          :validate-trigger="['change', 'blur']"
          hide-label
        >
          <a-select
            v-model="userInfo.selectedMediaId"
            style="width: 100%"
            placeholder="请选择邮箱地址"
          >
            <a-option
              v-for="media in resetPasswordResult.sysUserMedias"
              :key="media.id"
              :value="media.id"
            >
              {{ media.hiddenAccount }}
            </a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </div>
    <div v-else-if="currentStep === 'resetSuccess'">
      新的密码已发送到{{ resetPasswordResult.mailAddress }}请注意查收
    </div>
    <template #footer>
      <a-button
        v-if="currentStep === 'inputAccount'"
        type="primary"
        :loading="loading"
        @click="resetPassword"
      >
        重置密码
      </a-button>
      <a-button
        v-else-if="currentStep === 'selectEmail'"
        type="primary"
        :loading="loading"
        @click="resetPassword"
      >
        确认
      </a-button>
      <a-button
        v-else-if="['resetSuccess', 'noEmail'].includes(currentStep)"
        type="primary"
        @click="onCancel"
      >
        完成
      </a-button>
    </template>
  </a-modal>
</template>

<script setup lang="ts">
  import { ref } from 'vue';
  import {
    PasswordResetByEmail,
    resetPasswordByEmail,
  } from '@/api/system/user';
  import useLoading from '@/hooks/loading';

  defineProps({
    visible: {
      type: Boolean,
      required: true,
      default: false,
    },
  });

  const userInfo = ref({
    account: '',
    selectedMediaId: '',
  });

  const resetPasswordResult = ref<PasswordResetByEmail>({
    sysUserMedias: [],
    mailAddress: '',
  });

  const emitter = defineEmits(['close']);

  const currentStep = ref<string>('inputAccount');

  const { loading, setLoading } = useLoading(false);

  const resetPassword = async () => {
    try {
      setLoading(true);
      // 如果当前步骤为输入账户则从后台重置密码，如果该用户存在多个邮箱则进入选择邮箱步骤
      if (currentStep.value === 'inputAccount') {
        resetPasswordResult.value = (
          await resetPasswordByEmail({
            userAccount: userInfo.value.account,
          })
        ).data;
        if (resetPasswordResult.value.result === '1') {
          currentStep.value = 'resetSuccess';
        } else {
          currentStep.value = 'selectEmail';
        }
      } else if (currentStep.value === 'selectEmail') {
        resetPasswordResult.value = (
          await resetPasswordByEmail({
            userAccount: userInfo.value.account,
            selectedMediaId: userInfo.value.selectedMediaId,
          })
        ).data;
        if (resetPasswordResult.value.result === '1') {
          currentStep.value = 'resetSuccess';
        }
      }
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const onCancel = () => {
    userInfo.value = {
      account: '',
      selectedMediaId: '',
    };
    emitter('close');
  };
</script>
