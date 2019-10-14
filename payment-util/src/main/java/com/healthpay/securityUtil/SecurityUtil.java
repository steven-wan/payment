package com.healthpay.securityUtil;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-11 16:13
 */

import com.healthpay.securityUtil.security.*;
import org.apache.commons.codec.binary.Base64;

public final class SecurityUtil {
    private static final byte[] ENCRYPT_KEY = new byte[]{(byte) -81, (byte) 0, (byte) 105, (byte) 7, (byte) -32, (byte) 26, (byte) -49, (byte) 88};
    public static final String CHARSET = "UTF-8";

    public SecurityUtil() {
    }

    public static final byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    public static final String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    public static final String encryptMd5(String strSrc) {
        try {
            return MDCoder.encodeMD5(strSrc);
        } catch (Exception var2) {
            throw new RuntimeException("加密错误，错误信息：", var2);
        }
    }

    public static final String encryptSHA(String data) {
        try {
            return encryptBASE64(SHACoder.encodeSHA256(data.getBytes("UTF-8")));
        } catch (Exception var2) {
            throw new RuntimeException("加密错误，错误信息：", var2);
        }
    }

    public static final String encryptHMAC(byte[] data) {
        return encryptHMAC(data, ENCRYPT_KEY);
    }

    public static final String decryptDes(String cryptData, String key, String charset) {
        try {
            return new String(DESCoder.decrypt(decryptBASE64(cryptData), decryptBASE64(key)), charset);
        } catch (Exception var4) {
            throw new RuntimeException("解密错误，错误信息：", var4);
        }
    }

    public static final String decryptDes(String cryptData, byte[] key, String charset) {
        try {
            return new String(DESCoder.decrypt(decryptBASE64(cryptData), key), charset);
        } catch (Exception var4) {
            throw new RuntimeException("解密错误，错误信息：", var4);
        }
    }

    public static final String encryptDes(String data, String key, String charset) {
        try {
            return encryptBASE64(DESCoder.encrypt(data.getBytes(charset), decryptBASE64(key)));
        } catch (Exception var4) {
            throw new RuntimeException("加密错误，错误信息：", var4);
        }
    }

    public static final String encryptDes(String data, byte[] key, String charset) {
        try {
            return encryptBASE64(DESCoder.encrypt(data.getBytes(charset), key));
        } catch (Exception var4) {
            throw new RuntimeException("加密错误，错误信息：", var4);
        }
    }

    public static final String encryptHMAC(byte[] data, byte[] key) {
        try {
            return encryptBASE64(HmacCoder.encodeHmacSHA512(data, key));
        } catch (Exception var3) {
            throw new RuntimeException("加密错误，错误信息：", var3);
        }
    }

    public static final String signRSA(String data, String privateKey, String charset) {
        try {
            return encryptBASE64(RSACoder.sign(data.getBytes(charset), decryptBASE64(privateKey)));
        } catch (Exception var4) {
            throw new RuntimeException("签名错误，错误信息：", var4);
        }
    }

    public static final boolean verifyRSA(String data, String publicKey, String sign, String charset) {
        try {
            return RSACoder.verify(data.getBytes(charset), decryptBASE64(publicKey), decryptBASE64(sign));
        } catch (Exception var5) {
            throw new RuntimeException("验签错误，错误信息：", var5);
        }
    }

    public static final String encryptRSAPrivate(byte[] data, String privateKey) {
        try {
            return encryptBASE64(RSACoder.encryptByPrivateKey(data, decryptBASE64(privateKey)));
        } catch (Exception var3) {
            throw new RuntimeException("解密错误，错误信息：", var3);
        }
    }

    public static final byte[] decryptRSAPublic(String cryptData, String publicKey) {
        try {
            return RSACoder.decryptByPublicKey(decryptBASE64(cryptData), decryptBASE64(publicKey));
        } catch (Exception var3) {
            throw new RuntimeException("解密错误，错误信息：", var3);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encryptMd5(encryptMd5("123456")));
    }
}
