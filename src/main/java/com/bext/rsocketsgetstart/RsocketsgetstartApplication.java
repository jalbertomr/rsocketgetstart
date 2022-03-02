package com.bext.rsocketsgetstart;

import com.bext.rsocketsgetstart.entity.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@SpringBootApplication
@Controller
public class RsocketsgetstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(RsocketsgetstartApplication.class, args);
    }

    @MessageMapping("request-response")
    Mono<Message> requestResponse(final Message message){
        log.info("Received request-response message {}", message);
        return Mono.just(new Message("in controller message: " + message.getMessage()));
    }
}
