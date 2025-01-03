<template>
  <a-card :header-style="{ borderBottomWidth: '0px' }" title="端口信息">
    <template #extra>
      <a-radio-group v-model="timeFrom" @change="changeTimeFrom">
        <a-radio :value="1" style="margin-right: 0; padding-left: 0">
          <template #radio="{ checked }">
            <a-tag :checked="checked" checkable>一小时</a-tag>
          </template>
        </a-radio>
        <a-radio :value="24" style="margin-right: 0; padding-left: 0">
          <template #radio="{ checked }">
            <a-tag :checked="checked" checkable>一天</a-tag>
          </template>
        </a-radio>
        <a-radio :value="168" style="margin-right: 0; padding-left: 0">
          <template #radio="{ checked }">
            <a-tag :checked="checked" checkable>一周</a-tag>
          </template>
        </a-radio>
      </a-radio-group>
      <a-dropdown style="margin-left: 10px">
        <a-link :hoverable="false" style="font-size: 12px">自定义</a-link>
        <template #content>
          <a-doption @click="showPerformanceDraw('sendRate')">
            发送速率
          </a-doption>
          <a-doption @click="showPerformanceDraw('receiveRate')">
            接收速率
          </a-doption>
          <a-doption @click="showPerformanceDraw('error')">错丢包</a-doption>
        </template>
      </a-dropdown>
      <refresh-icon
        style="margin-bottom: -2px; margin-left: 10px"
        @click="fetchData"
      />
    </template>
    <a-spin :loading="loading" style="width: 100%">
      <a-row :gutter="[5, 5]">
        <a-col :span="16">
          <a-radio-group
            v-model="currentValue"
            type="button"
            @change="groupChange"
          >
            <a-radio value="important">
              重要({{ filterPortMap['important'].length }})
            </a-radio>
            <a-radio value="up">UP({{ filterPortMap['up'].length }})</a-radio>
            <a-radio value="down">
              DOWN({{ filterPortMap['down'].length }})
            </a-radio>
            <!--            <a-radio value="notImportant">-->
            <!--              非重要({{ filterPortMap['notImportant'].length }})-->
            <!--            </a-radio>-->
          </a-radio-group>
        </a-col>
        <a-col :span="8" style="text-align: right">
          <a-space>
            <a-input v-model="searchParam" placeholder="端口名称" />
            <a-button type="primary" @click="portConfigVisible = true">
              端口管理
            </a-button>
          </a-space>
        </a-col>
        <a-col :span="24">
          <a-radio-group
            v-if="filterPortMap[currentValue].length > 0"
            :model-value="checkedPort"
            size="medium"
          >
            <a-radio
              v-for="(port, index) in filterPortMap[currentValue]"
              :key="index"
              style="margin-right: 3px; padding-left: 0"
              :value="port.name"
            >
              <template #radio="{ checked }">
                <a-tag
                  class="tag"
                  style="padding-left: 0; margin-top: 5px"
                  :checked="checked"
                  checkable
                  size="medium"
                  @check="checkPort(port)"
                >
                  <icon-font
                    :style="{
                      marginRight: '3px',
                      color:
                        port.statusName === 'UP'
                          ? color('success-6')
                          : color('danger-6'),
                    }"
                    :size="16"
                    type="icon-duankou2"
                  />
                  {{ port.nickName }}
                </a-tag>
              </template>
            </a-radio>
          </a-radio-group>
          <a-empty v-else>
            <template #image></template>
            暂无端口
          </a-empty>
        </a-col>
        <a-col :span="12">
          <a-card :body-style="{ padding: 0 }">
            <performance-data-chart
              height="220px"
              :graph-data="graphData['sendRate']?.graph[0]"
              :single-line="true"
            />
          </a-card>
        </a-col>
        <a-col :span="12">
          <a-card :body-style="{ padding: 0 }">
            <performance-data-chart
              height="220px"
              :graph-data="graphData['receiveRate']?.graph[0]"
              :single-line="true"
            />
          </a-card>
        </a-col>
        <a-col :span="24">
          <a-card :body-style="{ padding: 0 }">
            <performance-data-chart
              height="220px"
              :graph-data="graphData['error']?.graph[0]"
              :single-line="false"
            />
          </a-card>
        </a-col>
      </a-row>
    </a-spin>
    <a-drawer
      width="40vw"
      :visible="portConfigVisible"
      title="端口管理"
      unmount-on-close
      :footer="false"
      @cancel="cancelPortConfig"
    >
      <port-list
        :height="'calc(100vh - 110px)'"
        :app-key="props.appKey"
        :host-id="props.hostId"
      />
    </a-drawer>
    <performance-data-draw
      :visible="performanceDrawSelectVisible"
      :app-key-item-ids="appKeyItemIds"
      @ok="performanceDrawSelectVisible = false"
      @cancel="performanceDrawSelectVisible = false"
    />
  </a-card>
</template>

<script setup lang="ts">
  import { computed, onMounted, ref, watch } from 'vue';
  import {
    getGraphPerformanceDataByPortName,
    getPortListByAppKeyAndHostIdGroupByStatus,
    Port,
    PortGroup,
  } from '@/api/monitor-object/port';
  import color from '@/utils/color';
  import _ from 'lodash';
  import PortList from '@/components/port-list/index.vue';
  import PerformanceDataChart from '@/views/monitor-manage/components/performance-data-draw/graph/performance-data-chart.vue';
  import { getLastHourRange } from '@/utils/date-time';
  import {
    AppKeyItemId,
    PerformanceDataViewVo,
  } from '@/api/monitor-object/item-instance';
  import RefreshIcon from '@/components/refresh-icon/index.vue';
  import PerformanceDataDraw from '@/views/monitor-manage/components/performance-data-draw/index.vue';

  const props = defineProps({
    appKey: {
      type: String,
      default: '',
    },
    hostId: {
      type: String,
      default: '',
    },
  });

  const portMap = ref<PortGroup>({
    important: [],
    up: [],
    down: [],
    notImportant: [],
  });

  const checkedPort = ref(undefined as string | undefined);
  const currentValue = ref('important');
  const searchParam = ref<string>('');
  const loading = ref<boolean>(false);
  const portConfigVisible = ref<boolean>(false);
  const timeFrom = ref<number>(1);
  const graphData = ref<{
    [key: string]: PerformanceDataViewVo;
  }>({});
  const performanceDrawSelectVisible = ref<boolean>(false);
  const appKeyItemIds = ref<AppKeyItemId[]>([]);

  const filterPortMap = computed(() => {
    const result: PortGroup = {
      important: [],
      up: [],
      down: [],
      notImportant: [],
    };
    result.important = _.filter(portMap.value.important, (port) =>
      port.name.toLowerCase().includes(searchParam.value.toLowerCase())
    );
    result.up = _.filter(portMap.value.up, (port) =>
      port.name.toLowerCase().includes(searchParam.value.toLowerCase())
    );
    result.down = _.filter(portMap.value.down, (port) =>
      port.name.toLowerCase().includes(searchParam.value.toLowerCase())
    );
    result.notImportant = _.filter(portMap.value.notImportant, (port) =>
      port.name.toLowerCase().includes(searchParam.value.toLowerCase())
    );
    return result;
  });
  //
  // const timeRange = computed(() => {
  //   return getLastHourRange(timeFrom.value);
  // });

  const checkPort = async (port: Port) => {
    checkedPort.value = port?.name;
    if (port && port.statusName === 'UP') {
      const param = _.cloneDeep(port);
      [param.timeFrom, param.timeTill] = getLastHourRange(timeFrom.value);
      try {
        loading.value = true;
        graphData.value = (await getGraphPerformanceDataByPortName(param)).data;
      } catch (e) {
        console.log(e);
      } finally {
        loading.value = false;
      }
    } else {
      graphData.value = {
        error: {
          showGraph: true,
          graph: [],
          text: [],
        } as PerformanceDataViewVo,
        receiveRate: {
          showGraph: true,
          graph: [],
          text: [],
        } as PerformanceDataViewVo,
        sendRate: {
          showGraph: true,
          graph: [],
          text: [],
        } as PerformanceDataViewVo,
      };
    }
  };

  const groupChange = (groupName: string) => {
    // @ts-ignore
    checkedPort.value = portMap.value[groupName][0]?.name;
    // @ts-ignore
    checkPort(portMap.value[groupName][0]);
  };

  const fetchData = async () => {
    try {
      loading.value = true;
      portMap.value = (
        await getPortListByAppKeyAndHostIdGroupByStatus(
          props.appKey,
          props.hostId
        )
      ).data;
      if (checkedPort.value) {
        const port = _.find(
          // @ts-ignore
          portMap.value[currentValue.value],
          (p) => p.name === checkedPort.value
        );
        await checkPort(port);
      } else {
        checkedPort.value = portMap.value.important[0]?.name;
        groupChange(currentValue.value);
      }
    } catch (e) {
      console.log(e);
    } finally {
      loading.value = false;
    }
  };

  const cancelPortConfig = () => {
    portConfigVisible.value = false;
    fetchData();
  };

  const changeTimeFrom = () => {
    const port = _.find(
      // @ts-ignore
      portMap.value[currentValue.value],
      (p) => p.name === checkedPort.value
    );
    checkPort(port);
  };

  const showPerformanceDraw = (type: string) => {
    const tempIds: AppKeyItemId[] = [];
    _.forEach(graphData.value[type]?.graph, (data) => {
      data.forEach((item) => {
        tempIds.push({
          appKey: item.appKey,
          itemId: item.itemid,
        });
      });
    });
    appKeyItemIds.value = _.cloneDeep(tempIds);
    performanceDrawSelectVisible.value = true;
  };

  watch(
    () => props.hostId,
    () => {
      fetchData();
    }
  );

  watch(
    () => props.appKey,
    () => {
      fetchData();
    }
  );

  onMounted(() => {
    fetchData();
  });
</script>

<style scoped lang="less">
  .arco-tag-checked {
    background: rgb(var(--info-2)) !important;
    //color: #fff !important;
  }

  .tag {
    background: var(--color-fill-2);
  }
</style>
