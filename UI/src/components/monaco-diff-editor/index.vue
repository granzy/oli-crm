<template>
  <div ref="monacoEditorRef" :style="monacoEditorStyle"></div>
</template>

<script setup lang="ts">
  import { computed } from 'vue';
  import { useMonacoDiffEditor } from '@/hooks/use-monaco-diff-editor';

  const props = withDefaults(
    defineProps<{
      width?: string | number;
      height?: string | number;
      language?: string;
      editorOption?: NonNullable<unknown>;
      originalValue: string | null;
      modifiedValue: string | null;
    }>(),
    {
      width: '100%',
      height: '100%',
      language: 'text',
      editorOption: () => ({}),
      originalValue: '',
      modifiedValue: '',
    }
  );

  const monacoEditorStyle = computed(() => {
    return {
      width: typeof props.width === 'string' ? props.width : `${props.width}px`,
      height:
        typeof props.height === 'string' ? props.height : `${props.height}px`,
    };
  });

  const {
    monacoEditorRef,
    createDiffEditor,
    getEditor,
    updateOptions,
    updateOriginalModel,
    updateModifiedModel,
  } = useMonacoDiffEditor(props.language);

  defineExpose({
    createDiffEditor,
    getEditor,
    updateOptions,
    updateOriginalModel,
    updateModifiedModel,
  });
</script>
