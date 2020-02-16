package com.weihuagu.receiptnotice.util;

import android.os.Build;

import com.weihuagu.receiptnotice.MainApplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class NetUtil {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();
    PreferenceUtil preferceutil = new PreferenceUtil(MainApplication.getAppContext());

    public String httppost(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        Request.Builder request = new Request.Builder()
                .url(url)
                .post(body);
        try (Response response = client.newCall(request.build()).execute()) {
            return response.body().string();
        }
    }

    public String httpspost(String url, String json) throws IOException {
        if (Build.VERSION.SDK_INT >= 21) {
            if (!preferceutil.isTrustAllCertificates())
                return httppost(url, json);
            else {
                RequestBody body = RequestBody.create(JSON, json);
                OkHttpClient clientwithtrustallcertificates = getHttpsClientWithTrustAllCertificates();
                Request.Builder request = new Request.Builder()
                        .url(url)
                        .post(body);
                try (Response response = clientwithtrustallcertificates.newCall(request.build()).execute()) {
                    return response.body().string();
                }
            }

        } else
            return doHttpsWithApi19(url, json);


    }

    public String doHttpsWithApi19(String url, String json) {
        try {
            RequestBody body = RequestBody.create(JSON, json);
            SSLSocketFactory factory = new SSLSocketFactoryCompat();
            ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .build();

            List<ConnectionSpec> specs = new ArrayList<>();
            specs.add(cs);
            specs.add(ConnectionSpec.COMPATIBLE_TLS);
            specs.add(ConnectionSpec.CLEARTEXT);


            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(20, TimeUnit.SECONDS)//设置读取超时时间
                    .sslSocketFactory(factory)
                    .connectionSpecs(specs)
                    .build();

            Request.Builder request = new Request.Builder()
                    .url(url)
                    .post(body);
            Response response = client.newCall(request.build()).execute();
            return response.body().string();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            LogUtil.debugLog(sw.toString());
            return null;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private OkHttpClient getHttpsClientWithTrustAllCertificates() {
        OkHttpClient.Builder okhttpClient = new OkHttpClient().newBuilder();
        //信任所有服务器地址
        okhttpClient. connectTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS);//设置读取超时时间
        okhttpClient.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                //设置为true
                return true;
            }
        });
        //创建管理器
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            //为OkHttpClient设置sslSocketFactory
            okhttpClient.sslSocketFactory(sslContext.getSocketFactory());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return okhttpClient.build();
    }
}
