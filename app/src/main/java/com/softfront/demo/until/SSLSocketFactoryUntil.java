package com.softfront.demo.until;

import android.content.Context;
import android.util.Log;

import com.softfront.demo.BuildConfig;
import com.softfront.demo.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * Created by nguyen.quang.tung on 4/14/2016.
 */
public class SSLSocketFactoryUntil {

    public static SSLSocketFactory createSSLSocketFactory(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        if (BuildConfig.DEBUG) {
            Log.d("AAA", "build developer");
            sslSocketFactory = createSSLSocketFactoryDeveloper(context);
        } else {
            Log.d("AAA", "build release");
            sslSocketFactory = createSSLSocketFactoryRelease(context);
        }
        return sslSocketFactory != null ? sslSocketFactory : createSSLSocketFactoryDeveloper(context);
    }

    public static SSLSocketFactory createSSLSocketFactoryDeveloper(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            final TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLSocketFactory createSSLSocketFactoryRelease(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS");
            InputStream inputStream = context.getResources().openRawResource(R.raw.appkeystore);
            keyStore.load(inputStream, AppUntil.getKeyHash(context).toCharArray());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }
}
