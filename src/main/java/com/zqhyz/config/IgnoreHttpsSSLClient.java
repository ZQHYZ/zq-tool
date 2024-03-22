//package com.zqhyz.config;
//
//import feign.Client;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//
//@Configuration
//public class IgnoreHttpsSSLClient {
//
//    @Bean
//    @ConditionalOnMissingBean
//    public Client feignSSLClient() throws Exception {
//        SSLContext ctx = SSLContext.getInstance("SSL");
//        X509TrustManager tm = new X509TrustManager(){
//
//            @Override
//            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//            }
//
//            @Override
//            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
//
//            }
//
//            @Override
//            public X509Certificate[] getAcceptedIssuers() {
////                return new X509Certificate[0];
//                return null;
//            }
//        };
//        ctx.init(null, new TrustManager[]{tm}, null);
//        return new Client.Default(ctx.getSocketFactory(), (hostname, session) -> true);
//    }
//}
