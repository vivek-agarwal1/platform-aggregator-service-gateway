//package com.oyorooms.gateway.core.config;
//
//import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.server.WebServerFactoryCustomizer;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TomcatConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
//
////    @Bean
////    public ConfigurableReactiveWebServerFactory webServerFactory() {
////        TomcatReactiveWebServerFactory factory = new TomcatReactiveWebServerFactory();
////        factory.addConnectorCustomizers(connector -> connector);
////        factory.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]"));
////        return factory;
////    }
//
//    @Override
//    public void customize(TomcatServletWebServerFactory factory) {
//        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
//            connector.setAttribute("relaxedQueryChars", "[]");
//        });
//    }
//}
