package com.healthpay.httpUtil;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-11 16:32
 */
public class KeyStoreBean {
    private String key;
    private String keyStoreType;
    private String path;
    private String password;

    public KeyStoreBean() {
    }

    public String getKeyStoreType() {
        return this.keyStoreType;
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
