<template>
  <div class="page-container">
    <div class="page-panel">
      <a-row>
        <a-col :span="14">
          <a-space style="margin-bottom: 5px">
            <a-input-search
              v-model="queryParams.queryStr"
              :style="{ width: '320px' }"
              placeholder="字典类型/名称/编码"
              allow-clear
              @search="search"
              @input="handleInputSearch"
              @clear="handleInputSearch"
            />
          </a-space>
        </a-col>
        <a-col :span="10" style="text-align: right">
          <a-space style="margin-bottom: 5px">
            <a-button type="primary" size="medium" @click="createSysDict">
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
    <a-modal
      :visible="visible"
      width="auto"
      title-align="start"
      :mask-closable="false"
      draggable
      @ok="handleSubmit"
      @cancel="visible = false"
    >
      <template #title>{{ form.id ? '编辑字典' : '新建字典' }}</template>
      <div>
        <a-form ref="submitFormRef" :model="form" :style="{ width: '600px' }">
          <a-form-item
            field="type"
            :label="$t('system.dict.filed.label.type')"
            :rules="[
              {
                required: true,
                message: $t('system.dict.filed.message.required.type'),
              },
              { maxLength: 64, message: '字典长度最大不能超过64' },
              {
                validator: validateTypeExists,
              },
            ]"
            validate-trigger="blur"
          >
            <a-input v-model="form.type" @blur="handleConfirmTypeCodeBlur" />
          </a-form-item>
          <a-form-item
            field="name"
            :label="$t('system.dict.filed.label.name')"
            :rules="[
              {
                required: true,
                message: $t('system.dict.filed.message.required.name'),
              },
            ]"
            validate-trigger="blur"
          >
            <a-input v-model="form.name" />
          </a-form-item>
          <a-form-item
            field="code"
            :label="$t('system.dict.filed.label.code')"
            :rules="[
              {
                required: true,
                message: $t('system.dict.filed.message.required.code'),
              },
              {
                validator: validateCodeExists,
              },
            ]"
            validate-trigger="blur"
          >
            <a-input v-model="form.code" />
          </a-form-item>
          <a-form-item
            field="description"
            :label="$t('system.dict.filed.label.description')"
          >
            <a-textarea
              v-model="form.description"
              show-word-limit
              :max-length="500"
            />
          </a-form-item>
        </a-form>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
  import { VxeGridInstance, VxeGridProps } from 'vxe-table';
  import { onMounted, reactive, ref } from 'vue';
  import useLoading from '@/hooks/loading';
  import { PaginationInstance, Message, Modal } from '@arco-design/web-vue';
  import _ from 'lodash';

  import {
    querySystemDictList,
    save,
    deleteDict,
    searchTagFormData,
    SystemDict,
    findSysDictByTypeAndCode,
  } from '@/api/system/dict';
  import page from '@/hooks/pagination';

  import debouncePromise from 'debounce-promise';

  const { loading, setLoading } = useLoading(false);

  const { pagination, pageSizeOptions } = page();
  const xGrid = ref<VxeGridInstance>();
  const xPagination = ref<PaginationInstance>();

  const gridOptions = ref<VxeGridProps<SystemDict>>({
    size: 'mini',
    height: 'auto',
    columnConfig: {
      resizable: true,
    },
    showOverflow: 'tooltip',
    columns: [
      { title: '字典类型', field: 'type', minWidth: 120 },
      { title: '字典名称', field: 'name', minWidth: 120 },
      { title: '字典编码', field: 'code', minWidth: 120 },
      { title: '描述', field: 'description', minWidth: 120 },
      { title: '创建时间', field: 'createTime', width: 200 },
      { title: '操作', width: 120, slots: { default: 'action' } },
    ],
  });

  const queryParams = reactive({
    queryStr: '',
  });
  const fetchData = async (
    params: searchTagFormData = {
      ...queryParams,
      page: pagination,
    }
  ) => {
    setLoading(true);
    const $grid = xGrid.value;
    try {
      const { data } = await querySystemDictList(params);
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

  const visible = ref(false);
  const submitFormRef = ref();

  const form = reactive<SystemDict>({
    id: '',
    type: '',
    name: '',
    code: '',
    description: '',
  });

  const handleSubmit = async () => {
    const validate = await submitFormRef.value.validate();
    if (!validate) {
      await save(form);
      Message.success('提交成功');
      visible.value = false;
      await fetchData();
    } else {
      // eslint-disable-next-line no-console
      console.warn('验证不通过');
    }
  };

  let rawRowType: string | undefined;
  let rawRowCode: string | undefined;

  const createSysDict = () => {
    visible.value = true;
    submitFormRef.value.resetFields();
    form.id = '';
    rawRowType = '';
    rawRowCode = '';
  };

  const edit = (row: SystemDict) => {
    visible.value = true;
    _.assign(form, row);
    rawRowType = form.type;
    rawRowCode = form.code;
    // 重置一下验证状态,否则之前的一些验证状态会存在
    submitFormRef.value.validate();
  };

  const deleteSysDict = async (row: SystemDict) => {
    Modal.warning({
      title: '提示',
      titleAlign: 'start',
      hideCancel: false,
      content: `此操作将永久删除字典'${row.name}'，是否继续?`,
      onOk: async () => {
        await deleteDict(row.id as string);
        Message.success('删除成功');
        await fetchData();
      },
    });
  };

  const handleInputSearch = debouncePromise(async () => {
    await fetchData();
  }, 500);

  onMounted(() => {
    fetchData();
  });

  let confirmTypeCodeDirty = false;
  const handleConfirmTypeCodeBlur = (e: any) => {
    const { value } = e.target;
    confirmTypeCodeDirty = confirmTypeCodeDirty || !!value;
  };
  // 表单校验规则
  const validateTypeExists = (value: string) => {
    if (value !== rawRowType && confirmTypeCodeDirty && form.code) {
      submitFormRef.value.validateField('code');
    }
  };

  const validateCodeExists = debouncePromise(
    async (value: string, callback: (error?: string) => void) => {
      if (value !== rawRowCode && form.type) {
        const { data: sysCode } = await findSysDictByTypeAndCode(
          form.type as string,
          value
        );
        if (sysCode) {
          callback('类型编码已存在');
        }
      }
    },
    500
  );
</script>

<style scoped lang="less"></style>
