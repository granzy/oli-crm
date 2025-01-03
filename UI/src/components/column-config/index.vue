<template>
  <a-button @click="open">列配置</a-button>
  <a-drawer
    width="30vw"
    :visible="visible"
    title="列配置"
    unmount-on-close
    :mask-closable="false"
    @before-open="init"
    @ok="save"
    @cancel="close"
  >
    <draggable v-model="tempColumnConfig" item-key="title">
      <template #item="{ element }">
        <div
          v-if="element.type !== 'checkbox' && element.title !== '操作'"
          class="field-draggable"
        >
          <span>⋮⋮</span>
          <a-checkbox v-model="element.visible">
            {{ `列名: ${element.title}` }}
          </a-checkbox>
        </div>
      </template>
    </draggable>
    <!--    <template v-for="(column, index) in mergedColumnConfig" :key="index">-->
    <!--     -->
    <!--    </template>-->
  </a-drawer>
</template>

<script setup lang="ts">
  import Draggable from 'vuedraggable';
  import { PropType, ref } from 'vue';
  import { VxeGridPropTypes } from 'vxe-table/types/grid';
  import _ from 'lodash';
  import { VxeTableDefines } from 'vxe-table/types/table';

  const emitter = defineEmits(['close', 'save']);
  const visible = ref<boolean>(false);
  const tempColumnConfig = ref<VxeGridPropTypes.Columns>([]);

  const props = defineProps({
    configId: {
      type: String,
      default: undefined,
      required: true,
    },
    columnConfig: {
      type: Array as PropType<VxeGridPropTypes.Columns>,
      default: undefined,
      required: true,
    },
  });
  const mergeColumnConfig = (
    configId: string | undefined,
    columnConfig: VxeGridPropTypes.Columns | undefined
  ) => {
    const configKey = `vxe_column_config_${configId}`;
    const localConfigStr = localStorage.getItem(configKey);
    let localConfigObj: VxeGridPropTypes.Columns = [];
    if (localConfigStr) {
      localConfigObj = JSON.parse(localConfigStr);
    }

    columnConfig?.forEach((column) => {
      column.visible = column.visible === undefined ? true : column.visible;
    });

    if (configId === undefined) {
      console.warn('列配置ID为空');
      return columnConfig || [];
    }
    if (columnConfig === undefined) {
      console.warn('初始列配置为空');
      return [];
    }
    if (localConfigObj.length === 0) {
      console.warn('本地存储的列配置为空');
      return columnConfig || [];
    }
    // 根据配置显示或隐藏列
    columnConfig.forEach((column: VxeTableDefines.ColumnOptions) => {
      const localColumn = localConfigObj.find(
        (item: VxeTableDefines.ColumnOptions) => item.field === column.field
      );
      if (localColumn) {
        column.visible =
          localColumn.visible === undefined ? true : localColumn.visible;
      }
    });

    // 如果columnConfig和localConfigObj都一致则返回localConfigObj否则返回columnConfig
    const columnConfigField = _.map(columnConfig, (column) => {
      if (column.field) {
        return column.field;
      }
      if (column.type === 'checkbox') {
        return 'checkbox';
      }
      console.warn('列配置数据存在问题除了checkbox外都需要field属性');
      return '';
    });

    const localConfigField = _.map(columnConfig, (column) => {
      if (column.field) {
        return column.field;
      }
      if (column.type === 'checkbox') {
        return 'checkbox';
      }
      console.warn('列配置数据存在问题除了checkbox外都需要field属性');
      return '';
    });

    if (columnConfig.length === localConfigObj.length) {
      // 如果两个数组的交集和自身数量一致则代表两个配置的项是相同的则返回local列配置否则返回新传入的列配置
      const allExistColumns = _.intersection(
        columnConfigField,
        localConfigField
      );
      if (allExistColumns.length === columnConfig.length) {
        columnConfig = localConfigObj;
      }
    }

    return columnConfig;
  };

  const init = () => {
    tempColumnConfig.value = mergeColumnConfig(
      props.configId,
      _.cloneDeep(props.columnConfig)
    );
  };

  const open = () => {
    visible.value = true;
  };

  const close = () => {
    visible.value = false;
    emitter('close', false);
  };

  const save = () => {
    localStorage.setItem(
      `vxe_column_config_${props.configId}`,
      JSON.stringify(tempColumnConfig.value)
    );
    emitter('save', tempColumnConfig.value);
    visible.value = false;
  };
  defineExpose({
    genActualColumnConfig: (
      configId: string,
      columnConfig: VxeGridPropTypes.Columns
    ) => {
      return mergeColumnConfig(configId, columnConfig);
    },
  });
</script>

<style lang="less" scoped>
  .field-draggable {
    cursor: move;
    padding: 5px;
    border: 1px solid var(--color-border-3);
    margin-bottom: 5px;
    span {
      vertical-align: top;
      font-weight: bold;
      color: var(--color-text-3);
    }
  }
</style>
