import { reactive } from 'vue';
import { Pagination } from '@/types/global';

export default function page() {
  const pagination = reactive<Pagination>({
    current: 1,
    pageSize: 50,
    orderByColumn: undefined,
    orderDirection: 'DESC',
    total: 0,
  });
  const pageSizeOptions = [20, 50, 100];
  return {
    pagination,
    pageSizeOptions,
  };
}
