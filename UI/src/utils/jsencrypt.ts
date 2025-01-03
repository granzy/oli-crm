// eslint-disable-next-line import/extensions
import JSEncrypt from 'jsencrypt/bin/jsencrypt.min';

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey =
  '-----BEGIN PUBLIC KEY-----\n' +
  'MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALWOUJ+7tFYxqTodGr7OCQCUifhIyKoI\n' +
  '+vWCJ1sxT4khDiPlX7h4e24oQNrUezZHZfHhVTrpsMjjQm3NSjnUrS0CAwEAAQ==\n' +
  '-----END PUBLIC KEY-----';

// 加密
export default function encrypt(txt: string): string {
  const encryptor = new JSEncrypt();
  encryptor.setPublicKey(publicKey); // 设置公钥
  return encryptor.encrypt(txt); // 对数据进行加密
}
