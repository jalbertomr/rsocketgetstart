package com.bext.rsocketsgetstart;

import com.bext.rsocketsgetstart.entity.Message;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.context.LocalRSocketServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RsocketsgetstartApplicationTests {
    private static RSocketRequester requester;

    @BeforeAll
    public static void setupOnce(@Autowired RSocketRequester.Builder builder,
                                 @LocalRSocketServerPort Integer port,
                                 @Autowired RSocketStrategies strategies) {
        requester = builder.connectTcp("localhost", port)
                .block();
    }

    @Test
    void requestGetResponteTest() {
        //Send a request message
        Mono<Message> response = requester
                .route("request-response")
                .data(new Message("MyMessage"))
                .retrieveMono(Message.class);

        //Verify that the response message contains the expected data
        StepVerifier
                .create(response)
                .consumeNextWith(message ->
                        assertThat(message.getMessage()).isEqualTo("in controller message: MyMessage"))
                .verifyComplete();
    }

    @Test
    void fireAndForgetTest(){
        //send a fire-and-forget message
        Mono<Void> response = requester
                .route("fire-and-forget")
                .data( new Message("MyMessage"))
                .retrieveMono(Void.class);
        //assert that the result is a completed mono
        StepVerifier
                .create(response)
                .verifyComplete();
    }
}
