<template>
  <a-cascader
    v-bind="$attrs"
    v-model="v"
    :trigger-props="{ updateAtScroll: true }"
    :field-names="{
      value: 'code',
      label: 'name',
    }"
    allow-clear
    :options="options"
  />
</template>

<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue';
  import { findCategoryTree } from '@/api/monitor-object/category';
  import { CascaderOption } from '@arco-design/web-vue';
  import _ from 'lodash';

  const props = defineProps({
    modelValue: {
      type: String,
    },
  });

  const emitter = defineEmits([
    'update:modelValue',
    'update',
    'getCategoryCode',
  ]);

  const options = ref<CascaderOption[]>([]);

  const getCategoryCode = (v: string) => {
    options.value.forEach((option) => {
      if (option.children) {
        const c = _.find(option.children, { code: v });
        if (c) {
          emitter('getCategoryCode', c.categoryCode);
        }
      }
    });
  };

  const v = computed({
    get() {
      return props.modelValue;
    },
    set(value) {
      emitter('update:modelValue', value);
      emitter('update', value);
      getCategoryCode(value as string);
    },
  });

  onMounted(() => {
    findCategoryTree().then((res) => {
      options.value = res.data;
    });
  });
</script>
