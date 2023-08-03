package ru.speedcam.security.config;

import org.apache.catalina.Context;
import org.apache.tomcat.websocket.server.WsSci;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.Collections;

;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/ws-broadcast");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/progress-ws").setAllowedOrigins("http://localhost").withSockJS();
        registry.addEndpoint("/progress-ws").setAllowedOrigins(String.valueOf(Collections.singletonList("*"))).withSockJS();
    }

//    @Bean
//    public TomcatEmbeddedServletContainerFactory tomcatContainerFactory() {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//        factory.setTomcatContextCustomizers(Arrays.asList(new TomcatContextCustomizer[] {
//                tomcatContextCustomizer()
//        }));
//        return factory;
//    }

    @Bean
    public TomcatContextCustomizer tomcatContextCustomizer() {
        return new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addServletContainerInitializer(new WsSci(), null);
            }
        };
    }

    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
//        registration.setMessageSizeLimit(500 * 1024);
        registration.setSendBufferSizeLimit(1024 * 1024 * 10000);
//        registration.setSendTimeLimit(20000);
    }
}