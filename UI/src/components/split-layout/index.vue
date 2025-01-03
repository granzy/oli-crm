<template>
  <a-row :gutter="10" :style="{ position: 'relative', height: height }">
    <div
      v-show="!showLeft"
      :style="{
        height: '100%',
        left: '16px',
        width: mode === 'fill' ? '1px' : '0px',
        position: 'absolute',
        zIndex: 9,
        background: 'var(--color-border-2)',
      }"
    >
      <span
        :class="{ toggleBtn: true, trapezoid: mode === 'space' }"
        :style="{
          background:
            mode === 'fill' ? 'var(--color-bg-7)' : 'var(--color-bg-2)',
        }"
        @click="toggleLeft"
      >
        <icon-caret-right />
      </span>
    </div>
    <a-col v-show="showLeft" v-bind="leftWidth" style="position: relative">
      <a-scrollbar
        :style="{
          height: height,
          overflow: 'auto',
        }"
      >
        <slot name="left" />
      </a-scrollbar>
      <div
        :style="{
          height: height,
          right: '0px',
          top: '0px',
          width: mode === 'fill' ? '1px' : '0px',
          position: 'absolute',
          zIndex: 9,
          background: 'var(--color-border-2)',
        }"
      ></div>
      <span
        :class="{ toggleBtn: true, trapezoid: mode === 'space' }"
        :style="{
          background:
            mode === 'fill' ? 'var(--color-bg-7)' : 'var(--color-bg-2)',
        }"
        @click="toggleLeft"
      >
        <icon-caret-left />
      </span>
    </a-col>
    <a-col
      v-bind="rightWidth"
      :style="{
        position: 'relative',
        paddingLeft: showLeft ? '10px' : '28px',
      }"
    >
      <a-scrollbar :style="{ height: height, overflow: 'auto' }">
        <slot name="right" />
      </a-scrollbar>
    </a-col>
  </a-row>
</template>

<script setup lang="ts">
  import { computed, onMounted, PropType, ref } from 'vue';
  import _ from 'lodash';

  type Mode = 'fill' | 'space';

  const props = defineProps({
    height: {
      type: String,
      default: '100%',
    },
    mode: {
      type: String as PropType<Mode>,
      default: 'fill',
    },
    leftWidth: {
      type: Object,
      default: () => ({
        span: 4,
      }),
    },
    leftIsShow: {
      type: Boolean,
      default: true,
    },
  });

  const emitter = defineEmits(['change', 'update:leftIsShow']);

  const showLeft = ref<boolean>(true);

  onMounted(() => {
    showLeft.value = _.cloneDeep(props.leftIsShow);
  });

  const rightWidth = computed(() => {
    const { leftWidth } = props;
    if (showLeft.value) {
      return {
        span: 24 - leftWidth.span,
        xxl: leftWidth.xxl ? 24 - leftWidth.xxl : undefined,
        xl: leftWidth.xl ? 24 - leftWidth.xl : undefined,
        lg: leftWidth.lg ? 24 - leftWidth.lg : undefined,
        md: leftWidth.md ? 24 - leftWidth.md : undefined,
        sm: leftWidth.sm ? 24 - leftWidth.sm : undefined,
        xs: leftWidth.xs ? 24 - leftWidth.xs : undefined,
      };
    }
    return {
      span: 24,
    };
  });

  const toggleLeft = () => {
    showLeft.value = !showLeft.value;
    emitter('change', showLeft.value);
    emitter('update:leftIsShow', showLeft.value);
  };

  defineExpose({
    toggleLeft,
  });
</script>

<style lang="less" scoped>
  .toggleBtn {
    cursor: pointer;
    width: 10px;
    height: 80px;
    border-radius: 20px;
    right: -4.5px;
    top: 30%;
    z-index: 10;
    position: absolute;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .trapezoid {
    border-radius: 0;
    clip-path: polygon(0% 0%, 100% 15%, 100% 85%, 0% 100%);
  }
</style>
