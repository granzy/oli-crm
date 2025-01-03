import { useRouter, RouteLocationRaw } from 'vue-router';

export default function useRouterUtils() {
  const router = useRouter();
  const openInNewTab = (option: RouteLocationRaw) => {
    window.open(router.resolve(option).href, '_blank');
  };
  return {
    openInNewTab,
  };
}
