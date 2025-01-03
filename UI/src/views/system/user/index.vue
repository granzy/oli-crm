<template>
  <div class="page-container">
    <div class="page-panel">
      <a-row>
        <a-col :span="14">
          <a-space style="margin-bottom: 5px">
            <a-input-search
              v-model="queryParams.queryStr"
              :style="{ width: '320px' }"
              placeholder="用户名称/登录名称"
              allow-clear
              @search="search"
              @input="handleInputSearch"
              @clear="handleInputSearch"
            />
          </a-space>
        </a-col>
        <a-col :span="10" style="text-align: right">
          <a-space style="margin-bottom: 5px">
            <a-button type="primary" size="medium" @click="createSysUser">
              {{ $t('button.create') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <div style="height: calc(100vh - 146px)">
        <vxe-grid ref="xGrid" :loading="loading" v-bind="gridOptions">
          <template #index="{ rowIndex }">
            {{ rowIndex + 1 + (pagination.current - 1) * pagination.pageSize }}
          </template>
          <template #phoneList="{ row }">
            {{ joinMedia(row, 'phone') }}
          </template>
          <template #wechatList="{ row }">
            {{ joinMedia(row, 'wechat') }}
          </template>
          <template #dingdingList="{ row }">
            {{ joinMedia(row, 'dingding') }}
          </template>
          <template #mailList="{ row }">
            {{ joinMedia(row, 'mail') }}
          </template>
          <template #status="{ row }">
            <span
              v-if="row.status === USER_STATUS.ENABLED"
              :style="{ color: allowLoginColor }"
            >
              允许
            </span>
            <span v-else :style="{ color: disAllowLoginColor }">禁止</span>
          </template>
          <template #action="{ row }">
            <a-space>
              <a-link @click="edit(row)">编辑</a-link>
              <a-link type="text" @click="deleteSysDict(row)">删除</a-link>
            </a-space>
          </template>
          <template #pager>
            <a-pagination
              ref="xPagination"
              :current="pagination.current"
              :page-size="pagination.pageSize"
              :total="pagination.total"
              :page-size-options="pageSizeOptions"
              style="padding-top: 10px"
              show-total
              show-jumper
              show-page-size
              @change="onPageChange"
              @page-size-change="onPageSizeChange"
            />
          </template>
        </vxe-grid>
      </div>
    </div>
    <a-drawer
      :visible="visible"
      width="50%"
      title-align="start"
      :ok-loading="submitLoading"
      :mask-closable="false"
      @ok="handleSubmit"
      @cancel="visible = false"
    >
      <template #title>{{ form.id ? '编辑用户' : '新建用户' }}</template>
      <div>
        <a-form
          ref="submitFormRef"
          autocomplete="off"
          :model="form"
          :style="{ width: '90%' }"
        >
          <a-form-item
            field="username"
            label="用户名称"
            :rules="[
              {
                required: true,
                message: '请输入名称',
              },
              { maxLength: 64, message: '名称长度最大不能超过64' },
            ]"
            validate-trigger="blur"
          >
            <a-input v-model="form.username" autocomplete="off" />
          </a-form-item>
          <a-form-item
            field="account"
            label="登录账号"
            :rules="[
              {
                required: true,
                message: '请输入登录账户',
              },
              { maxLength: 64, message: '登录账户长度最大不能超过64' },

              {
                validator: validateAccountExists,
              },
            ]"
            validate-trigger="blur"
          >
            <a-input v-model="form.account" autocomplete="off" />
          </a-form-item>
          <a-form-item
            v-if="!form.id"
            field="password"
            label="密码"
            :rules="[
              {
                required: !form.id,
                message: '请输入密码',
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
            validate-trigger="blur"
          >
            <a-input-password
              v-model.trim="form.password"
              autocomplete="new-password"
              @blur="handleConfirmPassBlur"
            />
          </a-form-item>
          <a-form-item
            v-if="!form.id"
            field="confirmPassword"
            label="确认密码"
            :rules="[
              {
                required: !form.id,
                message: '请输入确认密码',
              },
              {
                validator: compareToFirstPassword,
                message: '您输入的两个密码不一致',
              },
            ]"
            validate-trigger="blur"
          >
            <a-input-password
              v-model.trim="form.confirmPassword"
              autocomplete="new-password"
            />
          </a-form-item>
          <a-form-item
            v-for="(media, index) of form.phoneList"
            :key="index"
            :field="`phoneList[${index}].value`"
            :label="index === 0 ? '手机' : '手机' + index"
          >
            <a-input v-model="media.account" />
            <a-link
              v-if="
                (validatePhoneNumber(media.account) &&
                  form.phoneList &&
                  index === form.phoneList?.length - 1) ||
                (index === 0 && form.phoneList?.length === 1)
              "
              style="width: 40px"
              :disabled="!validatePhoneNumber(media.account)"
              :style="{ marginLeft: '10px' }"
              @click="handleAddMedia('phone')"
            >
              添加
            </a-link>
            <a-link
              v-if="
                !(
                  validatePhoneNumber(media.account) &&
                  form.phoneList &&
                  index === form.phoneList?.length - 1
                ) &&
                (index > 0 || (form.phoneList && form.phoneList?.length > 1))
              "
              style="width: 40px"
              :style="{ marginLeft: '10px' }"
              @click="handleDeleteMedia('phone', index)"
            >
              移除
            </a-link>
          </a-form-item>
          <a-form-item
            v-for="(media, index) of form.mailList"
            :key="index"
            :field="`mailList[${index}].value`"
            :label="index === 0 ? '邮箱' : '邮箱' + index"
          >
            <a-input v-model="media.account" />
            <a-link
              v-if="
                (validateMail(media.account) &&
                  form.mailList &&
                  index === form.mailList?.length - 1) ||
                (index === 0 && form.mailList?.length === 1)
              "
              style="width: 40px"
              :disabled="!validateMail(media.account)"
              :style="{ marginLeft: '10px' }"
              @click="handleAddMedia('mail')"
            >
              添加
            </a-link>
            <a-link
              v-if="
                !(
                  validateMail(media.account) &&
                  form.mailList &&
                  index === form.mailList.length - 1
                ) &&
                (index > 0 || (form.mailList && form.mailList.length > 1))
              "
              style="width: 40px"
              :style="{ marginLeft: '10px' }"
              @click="handleDeleteMedia('mail', index)"
            >
              移除
            </a-link>
          </a-form-item>
          <a-form-item
            v-for="(media, index) of form.wechatList"
            :key="index"
            :field="`wechatList[${index}].value`"
            :label="index === 0 ? '企业微信' : '企业微信' + index"
          >
            <a-input v-model="media.account" />
            <a-link
              v-if="
                (media.account !== '' &&
                  form.wechatList &&
                  index === form.wechatList.length - 1) ||
                (index === 0 && form.wechatList?.length === 1)
              "
              style="width: 40px"
              :disabled="media.account === ''"
              :style="{ marginLeft: '10px' }"
              @click="handleAddMedia('wechat')"
            >
              添加
            </a-link>
            <a-link
              v-if="
                !(
                  media.account !== '' &&
                  form.wechatList &&
                  index === form.wechatList.length - 1
                ) &&
                (index > 0 || (form.wechatList && form.wechatList?.length > 1))
              "
              style="width: 40px"
              :style="{ marginLeft: '10px' }"
              @click="handleDeleteMedia('wechat', index)"
            >
              移除
            </a-link>
          </a-form-item>
          <a-form-item
            v-for="(media, index) of form.dingdingList"
            :key="index"
            :field="`dingdingList[${index}].value`"
            :label="index === 0 ? '钉钉' : '钉钉' + index"
          >
            <a-input v-model="media.account" />
            <a-link
              v-if="
                (media.account !== '' &&
                  form.dingdingList &&
                  index === form.dingdingList.length - 1) ||
                (index === 0 && form.dingdingList?.length === 1)
              "
              style="width: 40px"
              :disabled="media.account === ''"
              :style="{ marginLeft: '10px' }"
              @click="handleAddMedia('dingding')"
            >
              添加
            </a-link>
            <a-link
              v-if="
                !(
                  media.account !== '' &&
                  form.dingdingList &&
                  index === form.dingdingList.length - 1
                ) &&
                (index > 0 ||
                  (form.dingdingList && form.dingdingList.length > 1))
              "
              style="width: 40px"
              :style="{ marginLeft: '10px' }"
              @click="handleDeleteMedia('dingding', index)"
            >
              移除
            </a-link>
          </a-form-item>
          <a-form-item field="status" label="用户状态" validate-trigger="blur">
            <a-checkbox v-model="form.status as boolean" :default-value="true">
              允许登录
            </a-checkbox>
          </a-form-item>
          <a-form-item
            field="description"
            label="描述"
            validate-trigger="blur"
            :rules="[{ maxLength: 500, message: '描述长度最大不能超过500' }]"
          >
            <a-textarea
              v-model="form.description"
              show-word-limit
              :max-length="500"
            />
          </a-form-item>
        </a-form>
      </div>
    </a-drawer>
  </div>
</template>

<script lang="ts" setup>
  import { VxeGridInstance, VxeGridProps } from 'vxe-table';
  import { computed, nextTick, onMounted, reactive, ref, Ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import {
    PaginationInstance,
    Message,
    Modal,
    CascaderOption,
  } from '@arco-design/web-vue';

  import _ from 'lodash';

  import {
    querySystemUserList,
    save,
    searchFormData,
    deleteSystemUser,
    SystemUser,
    SystemUserMedias,
    fetchSysUserByAccount,
  } from '@/api/system/user';
  import {
    validateMail,
    validatePhoneNumber,
    validatePasswordRegExp,
  } from '@/utils/validate';

  import page from '@/hooks/pagination';

  import { USER_STATUS } from '@/utils/constant';

  import debouncePromise from 'debounce-promise';
  import color from '@/utils/color';

  const { loading, setLoading } = useLoading(false);

  const allowLoginColor = color('success-6');
  const disAllowLoginColor = color('danger-6');

  const { loading: submitLoading, setLoading: setFormSubmitLoading } =
    useLoading(false);

  const { pagination, pageSizeOptions } = page();

  const xGrid = ref<VxeGridInstance>();
  const xPagination = ref<PaginationInstance>();

  const gridOptions = ref<VxeGridProps<SystemUser>>({
    size: 'mini',
    height: 'auto',
    columnConfig: {
      resizable: true,
    },
    showOverflow: 'tooltip',
    columns: [
      { title: '用户名称', field: 'username', minWidth: 120 },
      { title: '登录名称', field: 'account', minWidth: 120 },
      {
        title: '部门',
        minWidth: 120,
        slots: { default: 'orgGroupList' },
      },
      {
        title: '角色',
        minWidth: 120,
        slots: { default: 'userRoleList' },
      },
      {
        title: '允许登录',
        field: 'status',
        minWidth: 120,
        slots: { default: 'status' },
      },
      {
        title: '邮箱',
        minWidth: 120,
        slots: { default: 'mailList' },
      },
      {
        title: '手机',
        minWidth: 120,
        slots: { default: 'phoneList' },
      },
      {
        title: '企业微信',
        minWidth: 120,
        slots: { default: 'wechatList' },
      },
      {
        title: '钉钉',
        minWidth: 120,
        slots: { default: 'dingdingList' },
      },
      { title: '描述', field: 'description', minWidth: 120 },
      { title: '创建时间', field: 'createTime', width: 200 },
      {
        title: '操作',
        width: 120,
        fixed: 'right',
        slots: { default: 'action' },
      },
    ],
  });

  const queryParams = reactive({
    queryStr: '',
    orgGroupIds: [],
    userRoleId: '',
  });
  const fetchData = async (
    params: searchFormData = {
      ...queryParams,
      page: pagination,
    }
  ) => {
    setLoading(true);
    const $grid = xGrid.value;
    try {
      const { data } = await querySystemUserList(params);
      pagination.total = data.totalElements as number;
      if ($grid) {
        await $grid.reloadData(data.content);
      }
    } catch (err) {
      // eslint-disable-next-line no-console
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const onPageChange = (current: number) => {
    pagination.current = current;
    fetchData();
  };

  const onPageSizeChange = (pageSize: number) => {
    pagination.pageSize = pageSize;
    fetchData();
  };

  const search = () => {
    fetchData();
  };

  const handleInputSearch = debouncePromise(async () => {
    await fetchData();
  }, 500);

  const visible = ref(false);
  const submitFormRef = ref();

  const defaultMedias = (type: string) => {
    return [{ account: '', type, sortIndex: 0 }];
  };

  const form = ref<SystemUser>({
    id: '',
    account: '',
    username: '',
    password: '',
    confirmPassword: '',
    description: '',
    status: true,
    phoneList: defaultMedias('phone'),
    mailList: defaultMedias('mail'),
    wechatList: defaultMedias('wechat'),
    dingdingList: defaultMedias('dingding'),
    mediasList: [],
  });

  const submitForm = computed<SystemUser>(() => {
    const retForm = _.cloneDeep(form);
    retForm.value.status = form.value.status
      ? USER_STATUS.ENABLED
      : USER_STATUS.DISABLED;
    retForm.value.mediasList = [];
    if (form.value.phoneList) {
      retForm.value.mediasList = retForm.value.mediasList.concat(
        form.value.phoneList.filter((phone) => phone.account !== '')
      );
    }
    if (form.value.mailList) {
      retForm.value.mediasList = retForm.value.mediasList.concat(
        form.value.mailList.filter((mail) => mail.account !== '')
      );
    }
    if (form.value.wechatList) {
      retForm.value.mediasList = retForm.value.mediasList.concat(
        form.value.wechatList.filter((wechat) => wechat.account !== '')
      );
    }
    if (form.value.dingdingList) {
      retForm.value.mediasList = retForm.value.mediasList.concat(
        form.value.dingdingList.filter((dingding) => dingding.account !== '')
      );
    }
    delete retForm.value.phoneList;
    delete retForm.value.mailList;
    delete retForm.value.wechatList;
    delete retForm.value.dingdingList;
    return retForm.value;
  });

  const resetMediaFields = () => {
    form.value.phoneList = defaultMedias('phone');
    form.value.mailList = defaultMedias('mail');
    form.value.wechatList = defaultMedias('wechat');
    form.value.dingdingList = defaultMedias('dingding');
  };

  const handleSubmit = async () => {
    setFormSubmitLoading(true);
    const validate = await submitFormRef.value.validate();
    if (!validate) {
      await save(submitForm.value);
      Message.success(form.value.id ? '用户更新成功' : '用户保存成功');
      visible.value = false;
      await fetchData();
    } else {
      // eslint-disable-next-line no-console
      console.warn('验证不通过');
    }
    setFormSubmitLoading(false);
  };

  let rawRowAccount: string | undefined;

  const createSysUser = () => {
    // 新建的时候需要清空表单id字段,同时为了保证表单完全渲染之后再调用resetFields
    // 因为密码和重置密码表单使用了v-if进行渲染
    form.value.id = '';
    rawRowAccount = '';
    visible.value = true;
    nextTick(() => {
      submitFormRef.value.resetFields();
      resetMediaFields();
    });
  };

  const filterBlankMedia = (mediaList: SystemUserMedias[]) => {
    return mediaList.length === 1
      ? mediaList
      : mediaList.filter((phone) => phone.account !== '');
  };

  const suitForBlankMedia = (formValue: SystemUser) => {
    if (formValue.phoneList) {
      formValue.phoneList = filterBlankMedia(formValue.phoneList);
    }
    if (formValue.mailList) {
      formValue.mailList = filterBlankMedia(formValue.mailList);
    }
    if (formValue.wechatList) {
      formValue.wechatList = filterBlankMedia(formValue.wechatList);
    }
    if (formValue.dingdingList) {
      formValue.dingdingList = filterBlankMedia(formValue.dingdingList);
    }
  };

  const edit = (row: SystemUser) => {
    visible.value = true;
    resetMediaFields();
    _.assign(form.value, _.cloneDeep(row));
    rawRowAccount = _.cloneDeep(form.value.account);
    form.value.status = row.status === USER_STATUS.ENABLED;
    form.value.mediasList?.forEach((media) => {
      if (media.type === 'phone') {
        form.value.phoneList?.push(media);
      }
    });
    form.value.mediasList?.forEach((media) => {
      if (media.type === 'mail') {
        form.value.mailList?.push(media);
      }
    });
    form.value.mediasList?.forEach((media) => {
      if (media.type === 'wechat') {
        form.value.wechatList?.push(media);
      }
    });
    form.value.mediasList?.forEach((media) => {
      if (media.type === 'dingding') {
        form.value.dingdingList?.push(media);
      }
    });
    form.value.mediasList = [];
    suitForBlankMedia(form.value);
    // 重置一下验证状态,否则之前的一些验证状态会存在
    submitFormRef.value.validate();
  };

  const deleteSysDict = async (row: SystemUser) => {
    Modal.warning({
      title: '提示',
      titleAlign: 'start',
      hideCancel: false,
      content: `用户${row.username}删除后，所有关联该用户的策略将自动移除该用户，请谨慎操作。是否确认删除操作？`,
      onOk: async () => {
        await deleteSystemUser(row.id as string);
        Message.success('删除成功');
        await fetchData();
      },
    });
  };

  const handleAddMedia = (type: string) => {
    if (type === 'phone') {
      const sortIndex = form.value.phoneList?.length as number;
      form.value.phoneList?.push({ account: '', type, sortIndex });
    } else if (type === 'mail') {
      const sortIndex = form.value.mailList?.length as number;
      form.value.mailList?.push({ account: '', type, sortIndex });
    } else if (type === 'wechat') {
      const sortIndex = form.value.mailList?.length as number;
      form.value.wechatList?.push({ account: '', type, sortIndex });
    } else if (type === 'dingding') {
      const sortIndex = form.value.mailList?.length as number;
      form.value.dingdingList?.push({ account: '', type, sortIndex });
    }
  };

  const handleDeleteMedia = (type: string, index: number) => {
    if (type === 'phone') {
      form.value.phoneList?.splice(index, 1);
    } else if (type === 'mail') {
      form.value.mailList?.splice(index, 1);
    } else if (type === 'wechat') {
      form.value.wechatList?.splice(index, 1);
    } else if (type === 'dingding') {
      form.value.dingdingList?.splice(index, 1);
    }
  };

  const joinMedia = (row: SystemUser, type: string) => {
    return row.mediasList
      ?.filter((media) => media.type === type)
      .map((media) => media.account)
      .join(',');
  };

  const orgGroupTreeDataList: Ref<CascaderOption[]> = ref([]);
  const roleTreeDataList: Ref<CascaderOption[]> = ref([]);

  onMounted(() => {
    fetchData();
  });

  // 表单校验规则
  const validateAccountExists = debouncePromise(
    async (value: string, callback: (error?: string) => void) => {
      if (value !== rawRowAccount) {
        const { data: sysUser } = await fetchSysUserByAccount(value);
        if (sysUser) {
          callback('账户已存在');
        }
      }
    },
    500
  );

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

<style scoped lang="less"></style>
