<script setup lang="ts">
  import { ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { Message } from '@arco-design/web-vue';
  import {
    resetPassword,
    SysUserPasswordResetVo,
    validatePassword,
  } from '@/api/system/user';
  import debouncePromise from 'debounce-promise';
  import { useUserStore } from '@/store';
  import encrypt from '@/utils/jsencrypt';
  import { validatePasswordRegExp } from '@/utils/validate';

  export interface Props {
    visible: boolean;
  }
  withDefaults(defineProps<Props>(), {
    visible: false,
  });
  const emits = defineEmits(['ok', 'cancel']);

  const handleCancel = () => {
    emits('cancel');
  };

  const submitFormRef = ref();

  const form = ref<SysUserPasswordResetVo>({
    rawPassword: '',
    password: '',
    confirmPassword: '',
  });

  // 校验原密码
  const validateRawPassword = debouncePromise(
    async (value: string, callback: (error?: string) => void) => {
      const userStore = useUserStore();
      const { id: userId } = userStore;
      if (value) {
        const { data: resData } = await validatePassword({
          id: userId as string,
          rawPassword: encrypt(value),
        });
        if (!resData) {
          callback('原密码验证失败');
        } else {
          callback();
        }
      } else {
        callback('请输入原密码');
      }
    },
    500
  );

  const { loading: submitLoading, setLoading: setSubmitLoading } =
    useLoading(false);

  const handleOk = async () => {
    setSubmitLoading(true);
    const validate = await submitFormRef.value.validate();
    try {
      if (!validate) {
        const userStore = useUserStore();
        const { id: userId } = userStore;
        await resetPassword({
          id: userId as string,
          rawPassword: encrypt(form.value.rawPassword as string),
          password: encrypt(form.value.password as string),
          confirmPassword: encrypt(form.value.confirmPassword as string),
        });
        Message.success('提交成功');
        setSubmitLoading(false);
        emits('ok', form.value);
      } else {
        // eslint-disable-next-line no-console
        console.warn('验证不通过');
      }
    } catch (e) {
      console.error(e);
    } finally {
      setSubmitLoading(false);
    }
  };

  let confirmPassDirty = false;

  const handleConfirmPassBlur = (e: any) => {
    const { value } = e.target;
    confirmPassDirty = confirmPassDirty || !!value;
  };

  const validateToNextPassword = (
    value: string,
    callback: (error?: string) => void
  ) => {
    const confirmPasswordValue = form.value.confirmPassword;
    if (value && confirmPassDirty && confirmPasswordValue) {
      submitFormRef.value.validateField('confirmPassword');
    }
    callback();
  };
  const compareToFirstPassword = (
    value: string,
    callback: (error?: string) => void
  ) => {
    if (value && value !== form.value.password) {
      callback('您输入的两个密码不一致!');
    }
  };
</script>

<template>
  <a-modal
    unmount-on-close
    :visible="visible"
    width="500"
    title-align="start"
    title="修改密码"
    :mask-closable="false"
    draggable
    :ok-loading="submitLoading"
    @cancel="handleCancel"
    @ok="handleOk"
  >
    <a-form ref="submitFormRef" :model="form">
      <a-form-item
        field="rawPassword"
        label="原密码"
        :rules="[
          {
            required: true,
            message: '请输入原密码',
          },
          {
            validator: validateRawPassword,
          },
        ]"
        required
        validate-trigger="blur"
      >
        <a-input-password v-model.trim="form.rawPassword" />
      </a-form-item>
      <a-form-item
        field="password"
        label="新密码"
        required
        validate-trigger="blur"
        :rules="[
          {
            required: true,
            message: '请输入新密码',
          },
          {
            match: validatePasswordRegExp,
            message:
              '密码必须包含大小写字母,数字,特殊字符,至少8个字符，最多30个字符',
          },
          {
            validator: validateToNextPassword,
          },
        ]"
      >
        <a-input-password
          v-model.trim="form.password"
          autocomplete="new-password"
          @blur="handleConfirmPassBlur"
        />
      </a-form-item>
      <a-form-item
        field="confirmPassword"
        label="确认密码"
        required
        validate-trigger="blur"
        :rules="[
          {
            required: true,
            message: '请输入确认密码',
          },
          {
            validator: compareToFirstPassword,
            message: '您输入的两个密码不一致',
          },
        ]"
      >
        <a-input-password v-model.trim="form.confirmPassword" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped lang="less"></style>
