//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.healthpay.securityUtil.security;


import java.security.MessageDigest;

public abstract class MDCoder extends SecurityCoder {
    public MDCoder() {
    }

    public static byte[] encodeMD2(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD2");
        return md.digest(data);
    }

    public static byte[] encodeMD4(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD4");
        return md.digest(data);
    }

    public static String encodeMD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        char[] charArray = data.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int md5Bytes = 0; md5Bytes < charArray.length; ++md5Bytes) {
            byteArray[md5Bytes] = (byte)charArray[md5Bytes];
        }

        byte[] var8 = md.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();

        for(int i = 0; i < var8.length; ++i) {
            int val = var8[i] & 255;
            if(val < 16) {
                hexValue.append("0");
            }

            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

    public static byte[] encodeTiger(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("Tiger");
        return md.digest(data);
    }

    public static String encodeTigerHex(byte[] data) throws Exception {
        byte[] b = encodeTiger(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeWhirlpool(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("Whirlpool");
        return md.digest(data);
    }

    public static String encodeWhirlpoolHex(byte[] data) throws Exception {
        byte[] b = encodeWhirlpool(data);
        return new String(Hex.encode(b));
    }

    public static byte[] encodeGOST3411(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("GOST3411");
        return md.digest(data);
    }

    public static String encodeGOST3411Hex(byte[] data) throws Exception {
        byte[] b = encodeGOST3411(data);
        return new String(Hex.encode(b));
    }
}
