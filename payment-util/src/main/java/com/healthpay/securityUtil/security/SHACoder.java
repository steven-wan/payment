//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.healthpay.securityUtil.security;


import java.security.MessageDigest;

public abstract class SHACoder extends SecurityCoder {
    public SHACoder() {
    }

    public static byte[] encodeSHA(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        return md.digest(data);
    }

    public static byte[] encodeSHA1(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return md.digest(data);
    }

    public static byte[] encodeSHA256(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(data);
    }

    public static byte[] encodeSHA384(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-384");
        return md.digest(data);
    }

    public static byte[] encodeSHA512(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return md.digest(data);
    }

    public static byte[] encodeSHA224(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-224");
        return md.digest(data);
    }
}
