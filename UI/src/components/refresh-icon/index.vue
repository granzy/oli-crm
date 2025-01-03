<script setup lang="ts">
  import { ref } from 'vue';

  const props = defineProps({
    size: {
      type: Number,
      default: 18,
      required: false,
    },
    spinByAuto: {
      type: Boolean,
      default: true,
      required: false,
    },
  });

  const emits = defineEmits(['click']);

  const spin = ref(false);
  const handleRefresh = () => {
    spin.value = true;
    if (props.spinByAuto) {
      setTimeout(() => {
        spin.value = false;
      }, 500);
    }
    emits('click');
  };

  const setSpin = (status: boolean) => {
    spin.value = status;
  };

  defineExpose({
    setSpin,
  });
</script>

<template>
  <icon-font
    type="icon-shuaxin"
    :size="props.size"
    style="cursor: pointer; color: var(--color-text-2)"
    :spin="spin"
    @click="handleRefresh"
  />
</template>

<style scoped lang="less"></style>
