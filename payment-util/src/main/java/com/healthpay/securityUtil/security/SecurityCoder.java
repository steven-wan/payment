//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.healthpay.securityUtil.security;


import java.security.Security;

public abstract class SecurityCoder {
    private static Byte ADDFLAG = Byte.valueOf((byte)0);

    public SecurityCoder() {
    }

    static {
        if(ADDFLAG.byteValue() == 0) {
            Security.addProvider(new BouncyCastleProvider());
            ADDFLAG = Byte.valueOf((byte)1);
        }

    }
}
