//package com.zqhyz.config;
//
//import feign.Client;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
//import org.springframework.context.annotation.Bean;
//
//public class IgnoreHttpsFeignClientConfig {
//    @Bean
//    @ConditionalOnBean(IgnoreHttpsSSLClient.class)
//    public Client generateClient(IgnoreHttpsSSLClient ignoreHttpsSSLClient) throws Exception {
//        return ignoreHttpsSSLClient.feignSSLClient();
//    }
//}
