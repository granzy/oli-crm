package cn.oli.crm.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Rsa 工具类
 */
@Slf4j
public class RsaUtil {

    // Rsa 私钥
    public static String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAtY5Qn7u0VjGpOh0a\n" +
            "vs4JAJSJ+EjIqgj69YInWzFPiSEOI+VfuHh7bihA2tR7Nkdl8eFVOumwyONCbc1K\n" +
            "OdStLQIDAQABAkAx7cRgWTd76MZYM/7R8Elrfm3++M0uba/Oao+2NXZhCF6KFpTH\n" +
            "wAZPpzLD/eBIWLZ5IZTNPvQ2fG+FdPdXYWFlAiEA2zTKQkxLVWC4cnyJKHjsuF0F\n" +
            "74/pVVw3rHLV5dYo1SsCIQDUB7SmC/05Jbftr4SpQOXv9ZYC3e0s1DHMz0DmraoL\n" +
            "BwIgZNCRmQ43s2uJSPYnqpp63IvD2BrKyHxiCojCATOOkgMCIQCzc24eVulTc6vu\n" +
            "u+1wfl/drqHu/TteBJ29dO++DN9tAwIgb2Tx0WFcghc9vxX1BSPqqgG3Ii0pwBVM\n" +
            "iIyKgNVH1TY=";

    /**
     * 私钥解密
     *
     * @param text 待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String text) throws Exception {
        return decryptByPrivateKey(privateKey, text);
    }

    /**
     * 私钥解密
     *
     * @param privateKeyString 私钥
     * @param text             待解密的文本
     * @return 解密后的文本
     */
    public static String decryptByPrivateKey(String privateKeyString, String text) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyString));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(Base64.decodeBase64(text));
        return new String(result);
    }

}
