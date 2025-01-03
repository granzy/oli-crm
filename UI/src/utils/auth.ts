const TOKEN_KEY = 'token';

const isLogin = () => {
  return !!localStorage.getItem(TOKEN_KEY);
};

const getToken = () => {
  return localStorage.getItem(TOKEN_KEY);
};

const setToken = (token: string) => {
  localStorage.setItem(TOKEN_KEY, token);
};

const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY);
};

const isBuiltInRole = (roleId: string) => {
  return roleId === '2' || roleId === '3' || roleId === '4';
};

export { isLogin, getToken, setToken, clearToken, isBuiltInRole };
