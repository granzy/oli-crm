import { onMounted, onUnmounted } from 'vue';

const useInterval = (callback: (autoStart: boolean) => void, delay: number) => {
  let timer: any;

  const start = () => {
    timer = setInterval(callback, delay);
  };

  const stop = () => {
    clearInterval(timer);
  };

  onMounted(() => {
    start();
  });

  onUnmounted(() => {
    stop();
  });
  return {
    start,
    stop,
  };
};

export default useInterval;
