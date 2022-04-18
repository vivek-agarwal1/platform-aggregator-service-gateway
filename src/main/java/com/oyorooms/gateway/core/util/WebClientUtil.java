package com.oyorooms.gateway.core.util;

import com.oyorooms.gateway.configuration.entities.WebClientConfig;
import io.netty.channel.ChannelOption;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class WebClientUtil {

    @Autowired
    private WebClientConfig webClientConfig;

    public WebClient.Builder getHttpWebClientBuilder() {
        WebClient.Builder webClientBuilder = WebClient.builder();
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientConfig.getConnectTimeout())
                .responseTimeout(Duration.ofMillis(webClientConfig.getConnectionTimeout()))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(webClientConfig.getReadTimeout(),
                                        TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(webClientConfig.getWriteTimeout(),
                                        TimeUnit.MILLISECONDS)));
        webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient.wiretap("LoggingFilter",
                LogLevel.INFO, AdvancedByteBufFormat.TEXTUAL)));
        return webClientBuilder;
    }
}
