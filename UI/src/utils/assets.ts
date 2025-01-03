// eslint-disable-next-line import/prefer-default-export
export const getAssetsFile = (url: string, path?: string) => {
  const contextPath: string = path || 'images';
  return new URL(`../assets/${contextPath}/${url}`, import.meta.url).href;
};
