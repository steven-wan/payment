package com.healthpay.httpUtil;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-11 16:30
 */

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.KeyStore;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
    private static int MAXCONNECTION = 1200;
    private static int DEFAULTMAXCONNECTION = 30;
    private static int DEFAULTTIMETOLIVE = 3;
    private static PoolingHttpClientConnectionManager defaultConnectionManager = null;
    private static HttpClientBuilder httpBulder = null;
    private static RequestConfig requestConfig = null;
    private static Map<String, PoolingHttpClientConnectionManager> poolingHttpClientConnectionManagerMap;
    public static final String CONTENT_TYPE = "application/xml";
    public HttpClientUtil() {
    }

    public static void init() {
        init((List)null);
    }

    public static void init(List<KeyStoreBean> keyStoreBeans) {
        try {
            X509HostnameVerifier e = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
            requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(30000).setConnectionRequestTimeout(10000).build();
            if(null != keyStoreBeans && !keyStoreBeans.isEmpty()) {
                poolingHttpClientConnectionManagerMap = (Map)keyStoreBeans.parallelStream().collect(Collectors.toConcurrentMap(KeyStoreBean::getKey, (keyStoreBean) -> {
                    try {
                        KeyStore keyStore = KeyStore.getInstance(keyStoreBean.getKeyStoreType());
                        keyStore.load(HttpClientUtil.class.getResourceAsStream(keyStoreBean.getPath()), keyStoreBean.getPassword().toCharArray());
                        SSLContextBuilder sslContextBuilder = SSLContexts.custom().loadTrustMaterial(new TrustAllStrategy());
                        Registry socketFactoryRegistry = RegistryBuilder.create().register("https", new SSLConnectionSocketFactory(sslContextBuilder.loadKeyMaterial(keyStore, keyStoreBean.getPassword().toCharArray()).build(), e)).build();
                        return new PoolingHttpClientConnectionManager(socketFactoryRegistry);
                    } catch (Exception var5) {
                        return null;
                    }
                }));
            }

            Registry socketFactoryRegistry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
            defaultConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            httpBulder = HttpClients.custom().setMaxConnPerRoute(DEFAULTMAXCONNECTION).setMaxConnTotal(MAXCONNECTION).setConnectionTimeToLive((long)DEFAULTTIMETOLIVE, TimeUnit.MINUTES).setConnectionManager(defaultConnectionManager).setRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }

    private static CloseableHttpClient getConnection() {
        return getConnection(false, (String)null);
    }

    private static synchronized CloseableHttpClient getConnection(boolean useCert, String certKey) {
        if(null == httpBulder) {
            init();
        }

        if(useCert && StringUtils.isNotEmpty(certKey) && null != poolingHttpClientConnectionManagerMap) {
            httpBulder.setConnectionManager((HttpClientConnectionManager)poolingHttpClientConnectionManagerMap.get(certKey));
        } else {
            httpBulder.setConnectionManager(defaultConnectionManager);
        }

        return httpBulder.build();
    }

    public static HttpUriRequest getRequestMethod(Map<String, String> map, String url, String method) {
        List params = (List)map.entrySet().stream().map((x) -> {
            return new BasicNameValuePair((String)x.getKey(), (String)x.getValue());
        }).collect(Collectors.toList());
        return getRequestMethod(params, url, method);
    }

    public static HttpUriRequest getRequestMethod(List<NameValuePair> list, String url, String method) {
        RequestBuilder requestBuilder = null;
        if("post".equals(method)) {
            requestBuilder = RequestBuilder.post();
        } else {
            if(!"get".equals(method)) {
                throw new IllegalArgumentException("unsupported method :" + method);
            }

            requestBuilder = RequestBuilder.get();
        }

        requestBuilder.setUri(url);
        if(null != list && !list.isEmpty()) {
            requestBuilder.setEntity(new UrlEncodedFormEntity(list, Consts.UTF_8));
        }

        return requestBuilder.setConfig(requestConfig).build();
    }

    public static HttpUriRequest getRequestMethod(String data, String url, String method, String contentType) {
        RequestBuilder requestBuilder = null;
        if("post".equals(method)) {
            requestBuilder = RequestBuilder.post();
        } else {
            if(!"get".equals(method)) {
                throw new IllegalArgumentException("unsupported method :" + method);
            }

            requestBuilder = RequestBuilder.get();
        }

        StringEntity entity = null;
        if(null != contentType) {
            entity = new StringEntity(data, ContentType.create(contentType, Consts.UTF_8));
        } else {
            entity = new StringEntity(data, Consts.UTF_8);
        }

        return requestBuilder.setUri(url).addHeader("Connection", "close").setEntity(entity).setConfig(requestConfig).build();
    }

    public static String doPostMethod(String url, Map<String, String> map) throws Exception {
        CloseableHttpClient client = getConnection();
        HttpUriRequest post = getRequestMethod(map, url, "post");
        CloseableHttpResponse response = null;

        try {
            response = client.execute(post);
            if(response.getStatusLine().getStatusCode() != 200) {
                post.abort();
                throw new Exception("Http请求失败");
            }

            String e = EntityUtils.toString(response.getEntity(), "utf-8");
            return e;
        } catch (IOException var9) {
            logger.error("HTTP请求失败", var9);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return null;
    }

    public static String doPostMethod(String url, List<NameValuePair> list) throws Exception {
        CloseableHttpClient client = getConnection();
        HttpUriRequest post = getRequestMethod(list, url, "post");
        CloseableHttpResponse response = null;

        try {
            response = client.execute(post);
            if(response.getStatusLine().getStatusCode() != 200) {
                post.abort();
                throw new Exception("Http请求失败");
            }

            String e = EntityUtils.toString(response.getEntity(), "utf-8");
            return e;
        } catch (IOException var9) {
            logger.error("HTTP请求失败", var9);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return null;
    }

    public static final String httpPostSSL(String url, String data, boolean useCert, String certKey) throws Exception {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = getConnection(useCert, certKey);

        String var7;
        try {
            HttpUriRequest e = getPostMethod(url, data);
            response = httpClient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("Http请求失败");
            }

            var7 = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
            return var7;
        } catch (IOException var11) {
            logger.error("Http调用失败", var11);
            if(!(var11 instanceof SocketTimeoutException)) {
                throw new Exception(var11);
            }

            var7 = null;
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return var7;
    }

    public static final String httpPostSSL(String url, String data) throws Exception {
        return httpPostSSL(url, data, false, (String)null);
    }

    private static HttpUriRequest getPostMethod(String url, String data) {
        RequestBuilder requestBuilder = RequestBuilder.post().setUri(url);
        requestBuilder.setEntity(new StringEntity(data, "UTF-8"));
        return requestBuilder.setConfig(requestConfig).build();
    }

    private static HttpGet getGetMethod(String url) {
        HttpGet pmethod = new HttpGet(url);
        return pmethod;
    }

    public static final String httpPostSSLCert(String url, String data, String certPath, String certId) throws Exception {
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient = null;

        String var7;
        try {
            httpClient = getConnection();
            HttpUriRequest e = getPostMethod(url, data);
            response = httpClient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("Http请求失败");
            }

            var7 = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception var11) {
            logger.error("HTTPS连接失败", var11);
            throw new Exception("HTTPS连接失败");
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return var7;
    }

    public static final String httpPost(String url, String data, String contentType) throws Exception {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        String var6;
        try {
            httpclient = getConnection();
            HttpUriRequest e = getRequestMethod(data, url, "post", contentType);
            response = httpclient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("Http请求失败");
            }

            var6 = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception var10) {
            logger.error("http请求失败", var10);
            throw new Exception(var10);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return var6;
    }

    public static final String httpClientPost(String url, List<NameValuePair> list) throws Exception {
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse response = null;

        String var5;
        try {
            httpclient = getConnection();
            HttpUriRequest e = getRequestMethod(list, url, "post");
            response = httpclient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("网络请求失败");
            }

            var5 = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
        } catch (Exception var9) {
            logger.error("网络请求失败", var9);
            throw new Exception("网络请求失败", var9);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return var5;
    }

    public static byte[] httpGetAsByteArray(String url, List<NameValuePair> list) throws Exception {
        CloseableHttpClient httpClient = getConnection();
        CloseableHttpResponse response = null;
        Object bytes = null;

        byte[] bytes1;
        try {
            HttpUriRequest e = getRequestMethod(list, url, "get");
            response = httpClient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("网络请求失败");
            }

            HttpEntity httpEntity = response.getEntity();
            bytes1 = EntityUtils.toByteArray(httpEntity);
            EntityUtils.consume(httpEntity);
        } catch (IOException var10) {
            logger.error("网络请求失败", var10);
            throw new Exception("网络请求失败", var10);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return bytes1;
    }

    public static byte[] httpPostAsByteArray(String url, List<NameValuePair> list) throws Exception {
        CloseableHttpClient httpClient = getConnection();
        CloseableHttpResponse response = null;

        byte[] var7;
        try {
            HttpUriRequest e = getRequestMethod(list, url, "post");
            response = httpClient.execute(e);
            if(response.getStatusLine().getStatusCode() != 200) {
                e.abort();
                throw new Exception("网络请求失败");
            }

            HttpEntity httpEntity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(httpEntity);
            EntityUtils.consume(httpEntity);
            var7 = bytes;
        } catch (IOException var11) {
            logger.error("网络请求失败", var11);
            throw new Exception("网络请求失败", var11);
        } finally {
            if(null != response) {
                response.close();
            }

        }

        return var7;
    }
}

