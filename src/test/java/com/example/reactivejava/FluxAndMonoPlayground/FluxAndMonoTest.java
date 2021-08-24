package com.example.reactivejava.FluxAndMonoPlayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){

        Flux<String> stringFlux=Flux.just("Spring","Spring Boot")
                .map(s->s.concat(" Reactive"))
//                .concatWith(Flux.error(new RuntimeException("error occured")))
                .log();
        stringFlux.subscribe(System.out::println,
                (e)->System.err.println(e),
                ()->System.out.println("Event completed"));
    }
    @Test
    public void fluxTestElements_WithoutError(){
        Flux<String> stringFlux=Flux.just("Spring","Spring Boot")
                .log();
//        StepVerifier.create(stringFlux)
//                .expectNext("Spring")
//                .expectNext("Spring Boot")
//                .verifyComplete();// this method subscribes to publisher for flux
        //********************** Another way to write the same code *********************
        StepVerifier.create(stringFlux)
                .expectNext("Spring","Spring Boot")
                .verifyComplete();// this method subscribes to publisher for flux
    }

    @Test
    public void fluxTestElement_WithError(){
        Flux<String> stringFlux=Flux.just("Spring","Spring Boot")
                .concatWith(Flux.error(new RuntimeException("error occurred")))
                .log();
        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring Boot")
//                .expectError(RuntimeException.class);
                .expectErrorMessage("error occurred");
//                .verifyComplete(); when expect error is used verify complete should not be used like onComplete will not be executed if there is an error
    }

    @Test
    public void fluxTestElementCount_withoutError(){
        Flux<String> stringFlux=Flux.just("Spring","Spring Boot")
                .log();
        StepVerifier.create(stringFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void  MonoTest_withoutError(){
        Mono<String> stringMono=Mono.just("Spring")
                .log();
        StepVerifier.create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void MonoTest_withError(){
        Mono<Object> stringMono =Mono.error(new RuntimeException("error Occured"))
                .log();
        StepVerifier.create(stringMono)
                .expectError(RuntimeException.class);


    }
}
