import * as monaco from 'monaco-editor';
import { ref, onBeforeUnmount } from 'vue';
import { useAppStore } from '@/store';

const appStore = useAppStore();

// eslint-disable-next-line import/prefer-default-export
export function useMonacoDiffEditor(language = 'javascript') {
  // 编辑器实例
  let monacoDiffEditor: monaco.editor.IStandaloneDiffEditor | null = null;
  // 目标元素
  const monacoEditorRef = ref<HTMLElement | null>(null);

  // 创建实例
  function createDiffEditor(
    originalValue: string,
    modifiedValue: string,
    diffEditorOption: monaco.editor.IStandaloneDiffEditorConstructionOptions = {}
  ) {
    if (!monacoEditorRef.value) return;
    if (
      originalValue === null ||
      originalValue === undefined ||
      modifiedValue === null ||
      modifiedValue === undefined
    ) {
      console.error('Invalid input for diff editor');
      return;
    }
    monacoDiffEditor = monaco.editor.createDiffEditor(monacoEditorRef.value, {
      minimap: {
        enabled: true,
      },
      // 主题
      theme: appStore.theme === 'dark' ? 'vs-dark' : 'vs',
      // 主键
      multiCursorModifier: 'ctrlCmd',
      // 滚动条
      scrollbar: {
        verticalScrollbarSize: 8,
        horizontalScrollbarSize: 8,
      },
      // 字体大小
      fontSize: 12,
      ...diffEditorOption,
    });
    monacoDiffEditor.setModel({
      original: monaco.editor.createModel(originalValue, language),
      modified: monaco.editor.createModel(modifiedValue, language),
    });
  }

  function updateOriginalModel(originalValue: string) {
    monacoDiffEditor?.getModel()?.original?.setValue(originalValue);
  }

  function updateModifiedModel(modifiedValue: string) {
    monacoDiffEditor?.getModel()?.modified?.setValue(modifiedValue);
  }

  // 配置更新
  function updateOptions(
    opt: monaco.editor.IStandaloneEditorConstructionOptions
  ) {
    monacoDiffEditor?.updateOptions(opt);
  }

  // 获取实例
  function getEditor() {
    return monacoDiffEditor;
  }

  // 页面离开 销毁
  onBeforeUnmount(() => {
    if (monacoDiffEditor) {
      monacoDiffEditor.dispose();
    }
  });

  return {
    monacoEditorRef,
    createDiffEditor,
    getEditor,
    updateOptions,
    updateOriginalModel,
    updateModifiedModel,
  };
}
