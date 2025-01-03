package cn.oli.crm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * 加密工具
 *
 * @author csj
 */
public class EncryptionUtil {
    private static final Logger L = LoggerFactory.getLogger(EncryptionUtil.class);

    /**
     * 加密文件
     *
     * @param srcFile  待加密源文件
     * @param destFile 目标加密文件
     */
    public static void encryptFile(File srcFile, File destFile) {
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(srcFile.toPath()));
             BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(destFile.toPath()))) {
            byte[] data = new byte[1024];
            int len;
            while ((len = bis.read(data)) != -1) {
                byte[] temp = Arrays.copyOfRange(data, 0, len);
                bos.write(reverseArray(temp));
            }
            Files.deleteIfExists(srcFile.toPath());
        } catch (IOException e) {
            L.error("加密文件失败", e);
        }
    }

    /**
     * 解密文件
     *
     * @param encryptedFile 待解密加密文件
     * @return DecStr
     */
    public static String decryptFile(File encryptedFile) {
        StringBuilder buff = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(encryptedFile.toPath()))) {
            byte[] data = new byte[1024];
            int len;
            while ((len = bis.read(data)) != -1) {
                byte[] temp = Arrays.copyOfRange(data, 0, len);
                buff.append(new String(reverseArray(temp)));
            }
        } catch (IOException e) {
            L.error("解密文件失败", e);
        }
        return buff.toString();
    }

    /**
     * 字节数组的各位取反，并倒置
     *
     * @param bytes content
     * @return EncStr
     */
    private static byte[] reverseArray(byte[] bytes) {
        for (int i = 0; i < bytes.length / 2; i++) {
            byte b = (byte) ~bytes[i];
            bytes[i] = (byte) ~bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = b;
        }
        return bytes;
    }

}
