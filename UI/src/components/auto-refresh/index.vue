<template>
  <a-input-group>
    <a-button
      type="primary"
      size="medium"
      :loading="loading"
      @click="doRefresh"
    >
      <template #icon>
        <icon-refresh />
      </template>
    </a-button>
    <a-select
      v-model="currentInterval"
      :style="{ width: '86px' }"
      @change="changeInterval"
    >
      <a-option :value="0">停止</a-option>
      <a-option
        v-for="(interval, index) in intervalOptions"
        :key="index"
        :value="interval.value"
      >
        {{ interval.name }}
      </a-option>
    </a-select>
  </a-input-group>
</template>

<script setup lang="ts">
  import { onUnmounted, computed, ref } from 'vue';
  import useLoading from '@/hooks/loading';

  const props = defineProps({
    refresh: {
      type: Function,
      default() {
        return undefined;
      },
    },
    afterRefresh: {
      type: Function,
      default() {
        return undefined;
      },
    },
    modelValue: {
      type: Number,
      default: undefined,
    },
    intervalOptions: {
      type: Array,
      default() {
        return [
          {
            name: '30秒',
            value: 30000,
          },
          {
            name: '1分钟',
            value: 60000,
          },
          {
            name: '5分钟',
            value: 300000,
          },
        ];
      },
    },
  });

  const emitter = defineEmits(['update:modelValue']);
  const innerModelValue = ref<number>(0);

  const { loading, setLoading } = useLoading(false);
  const doRefresh = async () => {
    if (props.refresh !== undefined) {
      try {
        setLoading(true);
        await props.refresh();
        // eslint-disable-next-line no-empty
      } catch (e) {
      } finally {
        setLoading(false);
      }
      if (props.afterRefresh) {
        await props.afterRefresh();
      }
    }
  };

  let timer: any;

  const doClearInterval = () => {
    if (timer) {
      clearInterval(timer);
    }
  };
  const changeInterval = (val: number) => {
    // 如果timer存在清理掉
    doClearInterval();
    if (val !== 0) {
      timer = setInterval(doRefresh, val);
    }
  };

  const currentInterval = computed({
    get() {
      if (props.modelValue) {
        if (props.modelValue !== innerModelValue.value) {
          changeInterval(props.modelValue);
        }
        return props.modelValue;
      }
      return innerModelValue.value;
    },
    set(value) {
      innerModelValue.value = value;
      emitter('update:modelValue', value);
    },
  });

  onUnmounted(doClearInterval);
</script>
