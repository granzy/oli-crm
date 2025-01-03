<template>
  <div ref="monacoEditorRef" :style="monacoEditorStyle"></div>
</template>

<script setup lang="ts">
  import { onMounted, computed, watch } from 'vue';
  import { useMonacoEditor } from '@/hooks/use-monaco-editor';

  import JsonWorker from 'monaco-editor/esm/vs/language/json/json.worker?worker';
  import CssWorker from 'monaco-editor/esm/vs/language/css/css.worker?worker';
  import HtmlWorker from 'monaco-editor/esm/vs/language/html/html.worker?worker';
  import TsWorker from 'monaco-editor/esm/vs/language/typescript/ts.worker?worker';
  import EditorWorker from 'monaco-editor/esm/vs/editor/editor.worker?worker';

  // eslint-disable-next-line no-restricted-globals
  self.MonacoEnvironment = {
    getWorker(workerId, label) {
      if (label === 'json') {
        return new JsonWorker();
      }
      if (['css', 'scss', 'less'].includes(label)) {
        return new CssWorker();
      }
      if (['html', 'handlebars', 'razor'].includes(label)) {
        return new HtmlWorker();
      }
      if (['typescript', 'javascript'].includes(label)) {
        return new TsWorker();
      }
      return new EditorWorker();
    },
  };

  const props = withDefaults(
    defineProps<{
      width?: string | number;
      height?: string | number;
      language?: string;
      editorOption?: NonNullable<unknown>;
      modelValue: string;
    }>(),
    {
      width: '100%',
      height: '100%',
      language: 'javascript',
      editorOption: () => ({}),
      modelValue: '',
    }
  );

  const monacoEditorStyle = computed(() => {
    return {
      width: typeof props.width === 'string' ? props.width : `${props.width}px`,
      height:
        typeof props.height === 'string' ? props.height : `${props.height}px`,
    };
  });

  const { monacoEditorRef, createEditor, updateVal, updateOptions, getEditor } =
    useMonacoEditor(props.language);

  function updateMonacoVal(val: string) {
    if (val !== getEditor()?.getValue()) {
      updateVal(val);
    }
  }

  onMounted(() => {
    createEditor(props.editorOption);
    updateMonacoVal(props.modelValue);
  });

  watch(
    () => props.modelValue,
    () => {
      updateMonacoVal(props.modelValue);
    }
  );

  defineExpose({ updateOptions, getEditor });
</script>
