//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.healthpay.securityUtil.security;


import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public abstract class DESCoder extends SecurityCoder {
    public static final String KEY_ALGORITHM = "DES";
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5PADDING";

    public DESCoder() {
    }

    private static SecretKey toKey(byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeySpec k = new SecretKeySpec(key, "DES");
        return k;
    }

    public static byte[] decrypt(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        SecretKey k = toKey(key);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING", "BC");
        cipher.init(2, k);
        return cipher.doFinal(data);
    }

    public static byte[] encrypt(byte[] data, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, NoSuchProviderException {
        SecretKey k = toKey(key);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING", "BC");
        cipher.init(1, k);
        return cipher.doFinal(data);
    }

    public static byte[] initKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator kg = KeyGenerator.getInstance("DES", "BC");
        kg.init(64, new SecureRandom());
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

    static {
        if(null == Security.getProvider("BC")) {
            Security.addProvider(new BouncyCastleProvider());
        }

    }
}
