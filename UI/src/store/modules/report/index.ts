import { defineStore } from 'pinia';

const useReportStore = defineStore('report-module', {
  state: () => ({
    downloadList: [] as string[],
  }),

  getters: {
    isDownloading: (state) => state.downloadList.length > 0,
  },

  actions: {
    addDownloadItem(name: string) {
      this.downloadList.push(name);
    },
    delDownloadItem(name: string) {
      const idx = this.downloadList.indexOf(name);
      if (idx !== -1) {
        this.downloadList.splice(idx, 1);
      }
    },
  },
});

export default useReportStore;
