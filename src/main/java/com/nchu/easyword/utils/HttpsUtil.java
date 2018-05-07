package com.nchu.easyword.utils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsUtil {
    /*
     * 处理https GET/POST请求
     * 请求地址、请求方法、参数
     * */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = null;
        try {
            //创建SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if (null != outputStr) {
                OutputStream os = conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            buffer = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /*
    * https是对链接加了安全证书SSL的，如果服务器中没有相关链接的SSL证书，它就不能够信任那个链接，
    * 也就不会访问到了。所以我们第一步是自定义一个信任管理器。
    * 自要实现自带的X509TrustManager接口就可以了。
    * */
    static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
