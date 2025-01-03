<template>
  <div class="checked-border">
    <icon-font :type="modelValue" :size="size" @click="visible = true" />
    <a-modal
      title="选择图标"
      :top="'20vh'"
      :align-center="false"
      :mask-closable="false"
      :visible="visible"
      title-align="start"
      draggable
      @cancel="visible = false"
      @ok="
        emits('update:modelValue', innerModelValue);
        visible = false;
      "
      @close="innerModelValue = modelValue"
    >
      <a-row>
        <a-col v-for="(icon, i) in icons" :key="i" :span="4">
          <a-tooltip :content="icon.name">
            <icon-font
              :class="
                icon.icon === innerModelValue
                  ? 'checked-border'
                  : 'unChecked-border'
              "
              :type="icon.icon"
              :size="50"
              @click="check(icon)"
            ></icon-font>
          </a-tooltip>
        </a-col>
      </a-row>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { IconSelectType } from '@/components/icon-select/type';

  const props = defineProps({
    modelValue: {
      type: String,
      default: '',
    },
    size: {
      type: Number,
      default: 64,
      required: false,
    },
    icons: {
      type: Array,
      default: () => [],
      required: false,
    },
  });

  const emits = defineEmits(['update:modelValue']);

  const visible = ref<boolean>(false);

  // 默认值和父组件的modelValue一致
  const innerModelValue = ref<string>(props.modelValue);
  const check = (icon: IconSelectType) => {
    innerModelValue.value = icon.icon;
  };
</script>

<style scoped>
  .checked-border {
    border: 2px dashed;
    border-color: var(--color-border-3);
    padding: 5px;
    border-radius: 5px;
    transition: border-color 0.3s ease;
    cursor: pointer;
  }

  .unChecked-border {
    cursor: pointer;
  }

  .checked-border:hover {
    border-color: var(--color-border-4);
  }

  .unChecked-border:hover {
    border: 2px dashed;
    border-color: var(--color-border-3);
    padding: 5px;
    border-radius: 5px;
    cursor: pointer;
  }
</style>
