package com.example.reactivejava.Controller;

import com.example.reactivejava.Model.UserModel;
import org.junit.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import org.junit.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class FluxAndMonoControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void flux_approach1(){

        Flux<String> stringFlux = webTestClient.get().uri("/flux")
                .exchange()
                .expectStatus().isOk()
                .returnResult(String.class)
                .getResponseBody();

       stringFlux.subscribe(System.out::println);
    }

    @Test
    public void flux_approach2(){
        webTestClient.get().uri("/flux/stream")

                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/stream+json;charset=UTF-8")

                .expectBodyList(String.class)
                .hasSize(5);

    }
    @Test
    public void flux_approach3(){
        List<UserModel> expectedResult=List.of(
                UserModel.builder().username("Darshan").email("darshan@gmail.com").build(),
                UserModel.builder().username("Supriya").email("supram@gmail.com").build()
        );
        EntityExchangeResult<List<UserModel>> result=webTestClient.get().uri("/flux/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserModel.class)
                .returnResult();
        assertEquals(expectedResult,result.getResponseBody());

    }
    @Test
    public void flux_approach4(){
        List<UserModel> expectedResult=List.of(
                UserModel.builder().username("Darshan").email("darshan@gmail.com").build(),
                UserModel.builder().username("Supriya").email("supram@gmail.com").build()
        );
        EntityExchangeResult<List<UserModel>> result=webTestClient.get().uri("/flux/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserModel.class)
                .consumeWith(listEntityExchangeResult -> {
                    assertEquals(expectedResult,listEntityExchangeResult);
                });
        assertEquals(expectedResult,result.getResponseBody());

    }

    @Test
    @Timeout(value = 5000,unit = TimeUnit.MILLISECONDS)
    public void flux_test_Inf_stream(){
        Flux<Long> longFlux=webTestClient.get().uri("/flux/inf")
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();
        StepVerifier.create(longFlux)
                .expectNext(0L).expectNext(1L).expectNext(2L)
                .thenCancel()
                .verify();
    }

    @Test
    public void  mono_test(){
        UserModel expectedUser= UserModel.builder().username("Ashwin").email("ashwin@gmail.com").build();
        webTestClient.get().uri("/mono")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserModel.class)
                .consumeWith((userResponse)->{
                    assertEquals(expectedUser,userResponse.getResponseBody());
                });
    }
}
