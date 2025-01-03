const validateAccount = /^[a-zA-Z][\w|.]{1,45}$/;

const validatePasswordRegExp =
  /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])((?=.*?[^\w\s])|(?=.*?_)).{8,30}$/;

const validateMail = (value: string) => {
  return /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value);
};

const validatePhoneNumber = (value: string) => {
  return /^1[3-9]\d{9}$/.test(value);
};

// 正整数正则
const positiveNumberReg = /^[1-9]\d*$/;

// 中文字符正则
const chineseCharacterReg = /^[\u4E00-\u9FA5]+$/;

// IPv4
const ipv4Regex =
  /^(([1-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){1}(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){2}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$/;

export {
  validateAccount,
  validatePhoneNumber,
  validatePasswordRegExp,
  validateMail,
  positiveNumberReg,
  chineseCharacterReg,
  ipv4Regex,
};
