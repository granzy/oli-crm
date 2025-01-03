<template>
  <div
    style="
      overflow: hidden;
      border-radius: 4px;
      border: 1px solid #c0c0c0;
      padding: 0;
    "
  >
    <div class="slider-header">
      <div class="pdt-1">时间</div>
      <div class="pdt-1" style="border-bottom: 0">状态</div>
    </div>
    <div
      class="slider-body"
      :style="{ 'overflow-x': timeBuckets.length > 20 ? 'scroll' : 'hidden' }"
    >
      <div class="text-left flex font-12">
        <div style="width: 95%; position: relative; margin-left: 2rem">
          <div style="width: 100%" class="font-5 flex">
            <ul
              class="flex"
              style="
                width: 100%;
                position: absolute;
                top: 0.4rem;
                left: 0.4rem;
                margin-left: -2rem;
              "
            >
              <li v-for="(item, index) in timeBuckets" :key="index">
                <a-popover :content="item.startTime">
                  <div
                    class="text-overflow"
                    :style="{
                      position: 'absolute',
                      left: `${oneWidth * index}%`,
                    }"
                  >
                    {{
                      showHour
                        ? item.startTime.substring(11, 16)
                        : item.startTime.substring(5, 11)
                    }}
                  </div>
                </a-popover>
                <div
                  v-if="index === timeBuckets.length - 1"
                  class="text-overflow"
                  :style="{
                    position: 'absolute',
                    left: `${oneWidth * (index + 1)}%`,
                  }"
                >
                  {{
                    showHour
                      ? item.endTime.substring(11, 16)
                      : item.endTime.substring(5, 11)
                  }}
                </div>
              </li>
            </ul>
          </div>
          <div>
            <ul class="flex">
              <template v-for="(item, index) in timeBuckets" :key="index">
                <li
                  class="timescale"
                  :style="'position: absolute; left: ' + oneWidth * index + '%'"
                />
                <li
                  class="timeLine"
                  :style="{
                    position: 'absolute',
                    left: `${oneWidth * index}%`,
                    width: `${oneWidth}%`,
                  }"
                />
              </template>
              <li
                class="timescale"
                :style="{
                  position: 'absolute',
                  left: `${oneWidth * timeBuckets.length}%`,
                }"
              />
            </ul>
          </div>
        </div>
      </div>
      <div class="text-left flex font-12" style="margin-top: 14px">
        <div ref="mySlider" class="slider mgl-2">
          <div class="propo flex">
            <div v-for="(item, index) in timeBuckets" :key="index">
              <div
                class="slider-stop"
                :style="{
                  'left': `${oneWidth * index}%`,
                  'width': `${oneWidth}%`,
                  'background-color': backgroundColor[item.level],
                  'cursor': 'pointer',
                }"
                @click.stop="onSliderClick(index)"
              />
            </div>
          </div>
          <div ref="sliderButton" @mousedown="onButtonDown">
            <div class="right-btn">
              <div class="squre" />
              <div class="sanjiao" />
              <div class="line" />
            </div>
            <div ref="banner" class="banner flex" />
          </div>
        </div>
      </div>
    </div>
  </div>
  <div style="height: 1rem"></div>
  <div style="line-height: 2rem; font-weight: bold; text-align: center">
    <span>当前时间:</span>
    <span>{{ timescale.start }}-{{ timescale.end }}</span>
  </div>
</template>

<script setup lang="ts">
  /**
   * 使用示例：
   * <time-slider :buckets="buckets" @dragEnd="dragEnd" />
   *
   * 参数说明 buckets：滑块时间范围  dragEnd：当前选中的时间块
   */
  import { onMounted, ref, watch } from 'vue';
  import { TimeScale, TimeBucket } from '@/api/alert/alert-detail';
  import _ from 'lodash';

  const props = defineProps({
    showHour: {
      type: Boolean,
      default: false,
    },
    buckets: Array<TimeBucket>,
  });

  const emits = defineEmits<{
    (e: 'dragEnd', timescale: TimeScale): void;
  }>();

  const timescale = ref<TimeScale>({ start: '', end: '' });
  const timeBuckets = ref<Array<TimeBucket>>([]);

  watch(
    props,
    (newProp) => {
      if (newProp.buckets) {
        timeBuckets.value = _.cloneDeep(newProp.buckets);
        if (timeBuckets.value.length > 0) {
          timescale.value.start = timeBuckets.value[0].startTime;
          timescale.value.end = timeBuckets.value[0].endTime;
        }
      }
    },
    { deep: true, immediate: true }
  );

  const dragging = ref<boolean>(false);
  const isClick = ref<boolean>(false);
  const oneWidth = ref<number>(8.3);
  const newPercent = ref<number>(0);
  const targetTime = ref<string>('');

  const backgroundColor = ref({
    '-1': '#64C42D',
    '0': '#909399',
    '1': '#7499ff',
    '2': '#ffc859',
    '3': '#ffa059',
    '4': '#e97659',
    '5': '#e45959',
  });

  const banner = ref<any>(null);
  const sliderButton = ref<any>(null);
  const mySlider = ref<any>(null);

  const initSlider = () => {
    if (timeBuckets.value.length > 0) {
      oneWidth.value = Number(
        (100 / Math.min(timeBuckets.value.length, 20)).toFixed(10)
      );

      banner.value.style.width = `${oneWidth.value / 2}%`;
      banner.value.style.left = `${oneWidth.value / 4}%`;
      sliderButton.value.children[0].style.left = `${(
        oneWidth.value / 2
      ).toFixed(10)}%`;

      const start = timeBuckets.value[0].startTime;
      const end = timeBuckets.value[0].endTime;
      timescale.value = { start, end };
      targetTime.value = `${start}-${end}`;
    }
  };

  // 设置新的位置
  const setPosition = (isEmit: boolean) => {
    if (Number.isNaN(newPercent.value)) return;
    sliderButton.value.style.left = `${newPercent.value - oneWidth.value / 2}%`;
    sliderButton.value.children[0].style.left = `${newPercent.value}%`;
    sliderButton.value.children[1].style.left = `${
      newPercent.value - oneWidth.value / 4
    }%`;
    let i = Math.floor(newPercent.value / oneWidth.value);
    if (i < 0) {
      i = 0;
    }
    if (i >= timeBuckets.value.length) {
      i = timeBuckets.value.length - 1;
    }
    const start = timeBuckets.value[i].startTime;
    const end = timeBuckets.value[i].endTime;
    timescale.value = { start, end };
    targetTime.value = `${start}-${end}`;
    if (isEmit) {
      emits('dragEnd', timescale.value);
    }
  };

  const onDragging = (event: MouseEvent) => {
    if (dragging.value) {
      isClick.value = false;
      const length = mySlider.value.offsetWidth;
      let deltaX = event.pageX - mySlider.value.getBoundingClientRect().left;
      if (deltaX < (length * oneWidth.value) / 200) {
        deltaX = (length * oneWidth.value) / 200;
      }
      const maxX =
        ((oneWidth.value * timeBuckets.value.length -
          Number(oneWidth.value / 2)) /
          100) *
        length;

      if (deltaX > maxX) {
        deltaX = maxX;
      }
      newPercent.value = Number((deltaX / length).toFixed(10)) * 100;
      setPosition(false);
    }
  };

  // 滑动完成
  const onDragEnd = () => {
    if (dragging.value) {
      /*
       * 防止在 mouseup 后立即触发 click，导致滑块有几率产生一小段位移
       * 不使用 preventDefault 是因为 mouseup 和 click 没有注册在同一个 DOM 上
       */
      setTimeout(() => {
        dragging.value = false;
        if (!isClick.value) {
          setPosition(true);
        }
      }, 100);
      window.removeEventListener('mousemove', onDragging);
      window.removeEventListener('mouseup', onDragEnd);
    }
  };

  const onDragStart = () => {
    dragging.value = true;
    isClick.value = true;
  };

  // 点击到相应时间段
  const onSliderClick = (index: number) => {
    newPercent.value = Number(
      (oneWidth.value * index + Number(oneWidth.value / 2)).toFixed(10)
    );
    setPosition(true);
  };

  // 鼠标按下
  const onButtonDown = (event: MouseEvent) => {
    event.preventDefault();
    onDragStart();
    window.addEventListener('mousemove', onDragging);
    window.addEventListener('mouseup', onDragEnd);
  };

  onMounted(() => {
    initSlider();
  });
</script>

<style scoped lang="less">
  .text-left {
    text-align: left;
  }
  .fs-tab {
    margin: 0;
    border-bottom: 1px solid #e3e8ef;
  }
  .fs-tab li {
    position: relative;
    float: left;
    margin-right: 25px;
    padding: 0 3px;
    line-height: 35px;
    list-style: none;
    font-size: 14px;
    cursor: pointer;
  }
  .fs-tab li.on {
    font-size: 14px;
    font-weight: bold;
    color: #0089c8;
  }
  .fs-tab li.on:after {
    content: '';
    position: absolute;
    bottom: -1px;
    left: 0;
    width: 100%;
    height: 3px;
    background-color: #0089c8;
  }
  .clearfix:after {
    content: '.';
    display: block;
    height: 0;
    clear: both;
    visibility: hidden;
  }
  .slider {
    width: 95%;
    position: relative;
    height: 0.5rem;
    top: 0.8rem;
  }
  .slider.propo-bg {
    background: #b6b6b6;
    border-radius: 0.04rem;
    height: 0.04rem;
    width: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 0;
  }
  .right-btn {
    position: absolute;
    top: -3rem;
    transform: translate(-50%, 0);
    cursor: grab;
  }
  .squre {
    content: '';
    height: 0.5rem;
    position: absolute;
    top: 0.82rem;
    left: -0.35rem;
    width: 0.8rem;
    background: #b6b6b6;
    border-bottom-left-radius: 0.1rem;
    border-bottom-right-radius: 0.1rem;
  }
  .sanjiao {
    content: '';
    position: absolute;
    top: 1.3rem;
    left: -0.35rem;
    z-index: 999;
    display: block;
    width: 0;
    height: 0.4rem;
    border-left: 0.4rem solid transparent;
    border-right: 0.4rem solid transparent;
    border-top: 0.4rem solid #b6b6b6;
  }
  .line {
    position: absolute;
    height: 1.7rem;
    width: 1px;
    background: #c0c4cc;
    top: 1.6rem;
  }
  .propo {
    width: 100%;
    height: 0.4rem;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
  }
  .left-btn {
    position: absolute;
    top: -0.45rem;
    transform: translate(-50%, 0);
  }
  .left-btn:before {
    content: '';
    display: block;
    height: 0.3rem;
    width: 0.34rem;
    background: #b6b6b6;
    border-bottom-left-radius: 0.1rem;
    border-bottom-right-radius: 0.1rem;
  }
  .left-btn:after {
    content: '';
    display: block;
    width: 0;
    height: 0.4rem;
    border-left: 0.17rem solid transparent;
    border-right: 0.17rem solid transparent;
    border-top: 0.17rem solid #b6b6b6;
  }

  .slider.active:before {
    border-bottom-color: #ff8c30;
  }
  .slider.active:after {
    background: #ff8c30;
  }
  .slider-stop {
    position: absolute;
    height: 0.4rem;
    border-right: 3px solid rgba(255, 255, 255, 1);
  }
  .flex {
    display: flex;
    justify-content: flex-start;
  }
  .font-5 {
    font-size: 5px;
  }
  .timescale {
    height: 0.2rem;
    width: 1px;
    background: lightgray;
    position: absolute;
    top: 2rem;
  }
  .timeLine {
    height: 1px;
    position: absolute;
    top: 2.2rem;
    background: lightgray;
  }
  ul {
    list-style: none;
  }
  .font-14 {
    font-size: 14px;
  }
  .font-12 {
    font-size: 12px;
  }
  .pdl-2 {
    padding-left: 2rem;
  }
  .mgl-2 {
    margin-left: 2rem;
  }
  .pdt-1 {
    text-align: center;
    width: 40px;
    height: 40px;
    line-height: 40px;
    background-color: #e6e6e6;
    border-right: 1px solid #c0c0c0;
    border-bottom: 1px solid #c0c0c0;
    writing-mode: vertical-rl;
  }
  .slider-header {
    float: left;
    width: 40px;
    height: 80px;
  }
  .slider-body {
    margin-left: 42px;
    height: 80px;
  }
  div {
    box-sizing: border-box;
  }
  .banner {
    z-index: 1000;
    position: absolute;
    top: -4px;
    width: 8.3%;
    height: 0.8rem;
    background: #86d378;
    text-align: center;
    border-radius: 25px;
    color: white;
    cursor: grab;
  }
  .text-overflow {
    width: 3rem;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .text-overflow:hover {
    cursor: pointer;
  }
</style>
