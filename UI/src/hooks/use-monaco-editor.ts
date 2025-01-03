import * as monaco from 'monaco-editor';
import { ref, nextTick, onBeforeUnmount } from 'vue';
import { useAppStore } from '@/store';

const appStore = useAppStore();

monaco.languages.register({ id: 'log' });
monaco.languages.setMonarchTokensProvider('log', {
  defaultToken: '',
  tokenPostfix: '.log',
  tokenizer: {
    root: [],
  },
});

// eslint-disable-next-line import/prefer-default-export
export function useMonacoEditor(language = 'javascript') {
  // 编辑器示例
  let monacoEditor: monaco.editor.IStandaloneCodeEditor | null = null;
  // 目标元素
  const monacoEditorRef = ref<HTMLElement | null>(null);

  // 创建实例
  function createEditor(
    editorOption: monaco.editor.IStandaloneEditorConstructionOptions = {}
  ) {
    if (!monacoEditorRef.value) return;
    monacoEditor = monaco.editor.create(monacoEditorRef.value, {
      // 初始模型
      model: monaco.editor.createModel('', language),
      // 主题
      theme: appStore.theme === 'dark' ? 'vs-dark' : 'vs',
      multiCursorModifier: 'ctrlCmd',
      // 滚动条
      scrollbar: {
        verticalScrollbarSize: 8,
        horizontalScrollbarSize: 8,
      },
      fontSize: 12,
      // 自动布局
      automaticLayout: true,
      ...editorOption,
    });
    // 设置多行文本换行统一为\n
    monacoEditor.getModel()?.setEOL(monaco.editor.EndOfLineSequence.LF);
  }

  // 格式化
  async function formatDoc() {
    await monacoEditor?.getAction('editor.action.formatDocument')?.run();
  }

  // 数据更新
  function updateVal(val: string) {
    nextTick(() => {
      monacoEditor?.setValue(val);
      setTimeout(async () => {
        await formatDoc();
      }, 10);
    });
  }

  // 配置更新
  function updateOptions(
    opt: monaco.editor.IStandaloneEditorConstructionOptions
  ) {
    monacoEditor?.updateOptions(opt);
  }

  // 获取配置
  function getOption(name: monaco.editor.EditorOption) {
    return monacoEditor?.getOption(name);
  }

  // 获取实例
  function getEditor() {
    return monacoEditor;
  }

  // 页面离开 销毁
  onBeforeUnmount(() => {
    if (monacoEditor) {
      monacoEditor.dispose();
    }
  });

  return {
    monacoEditorRef,
    createEditor,
    getEditor,
    updateVal,
    updateOptions,
    getOption,
    formatDoc,
  };
}
