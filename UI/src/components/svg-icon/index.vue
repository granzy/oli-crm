<script lang="ts" setup>
  import { compile, computed, defineAsyncComponent, h } from 'vue';
  import { ShadowRoot } from 'vue-shadow-dom';

  export interface Props {
    icon: string;
    size?: string;
    color?: string;
    className?: string;
    isMenu?: boolean;
  }
  const props = withDefaults(defineProps<Props>(), {
    size: '1.2em',
    isMenu: false,
  });

  const icons = import.meta.glob(`@/assets/svg/*.svg`);

  const dynamicComponent = computed(() => {
    if (props.icon.startsWith('icon-')) {
      return h(compile(`<${props.icon} />`));
    }
    return defineAsyncComponent(
      () => icons[`/src/assets/svg/${props.icon}.svg`]() as any
    );
  });

  const svgStyle = computed(() => {
    let style = '';
    if (props.size) style += `width: ${props.size};`;
    if (props.size) style += `height: ${props.size};`;
    if (props.color) style += `color: ${props.color};`;
    return style;
  });

  const svgContainerStyle = computed(() => {
    let style = '';
    if (props.size) style += `width: ${props.size};`;
    if (props.size) style += `height: ${props.size};`;
    return style;
  });

  const svgContainerClass = computed(() => {
    let baseClass = 'svg-icon';
    if (props.isMenu) {
      baseClass = 'menu-icon';
    }
    if (props.className) {
      return `${baseClass} ${props.className}`;
    }
    return baseClass;
  });
</script>

<template>
  <shadow-root :class="svgContainerClass" tag="span" :style="svgContainerStyle">
    <component :is="dynamicComponent" :style="svgStyle" />
  </shadow-root>
</template>

<style lang="less">
  .svg-icon {
    margin-right: 0.4em;
  }
</style>
