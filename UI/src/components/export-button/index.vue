<template>
  <a-dropdown @select="handleSelect">
    <a-button :loading="loading">导出</a-button>
    <template #content>
      <a-doption v-if="showAll" value="all">导出全部</a-doption>
      <a-doption
        v-if="showSelected"
        :disabled="selectedData?.length === 0"
        value="selected"
      >
        导出已选内容
      </a-doption>
      <a-doption v-if="showCondition" value="condition">导出条件内容</a-doption>
      <slot />
    </template>
  </a-dropdown>
</template>

<script setup lang="ts">
  const props = defineProps({
    selectedData: {
      type: Array,
      default() {
        return [];
      },
    },
    showAll: {
      type: Boolean,
      default: true,
      required: false,
    },
    showSelected: {
      type: Boolean,
      default: true,
      required: false,
    },
    showCondition: {
      type: Boolean,
      default: true,
      required: false,
    },
    loading: {
      type: Boolean,
      default: false,
    },
  });

  const emits = defineEmits(['export']);

  const handleSelect = (action: 'all' | 'selected' | 'condition') => {
    emits('export', action, props.selectedData);
  };
</script>
