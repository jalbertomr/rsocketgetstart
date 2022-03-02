package com.bext.rsocketsgetstart;

import com.bext.rsocketsgetstart.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@SpringBootApplication
@Controller
public class RsocketsgetstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsocketsgetstartApplication.class, args);
    }

    @MessageMapping("request-response")
    Mono<Message> requestResponse(final Message message){
        Hooks.onErrorDropped(error->log.warn("Exception happened: {}", error.getMessage()));
        log.info("Received request-response message {}", message);
        return Mono.just(new Message("in controller message: " + message.getMessage()));
    }

    @MessageMapping("fire-and-forget")
    public Mono<Void> fireAndforget(final Message message){
        log.info("-> fire-and-forget request: {}", message);
        return Mono.empty();
    }

    @MessageMapping("stream-request")
    public Flux<Message> stream(final Message message){
        Hooks.onErrorDropped(error->log.warn("Exception happened: {}", error.getMessage()));
        log.info("-> stream-request: {}", message);
        return Flux
                .interval(Duration.ofSeconds(1))
                .map(index -> new Message("in controller message: " + message.getMessage() + " request #: " + index))
                .log();
    }

    @MessageMapping("request-channel")
    public Flux<Message> channel(final Flux<Integer> settings){
        Hooks.onErrorDropped(error->log.warn("Exception happened: {}", error.getMessage()));
        log.info("Received request-channel");

        return settings
                .doOnNext(setting -> log.info("Requested interval is {} seconds.", setting))
                .doOnCancel(() -> log.warn("The client cancel the channel."))
                .switchMap(setting -> Flux.interval(Duration.ofSeconds( setting)))
                .map(index -> new Message("channel generating response #" + index))
                .log();
    }
}
