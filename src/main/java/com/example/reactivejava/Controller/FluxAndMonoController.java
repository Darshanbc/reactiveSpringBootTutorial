package com.example.reactivejava.Controller;


import com.example.reactivejava.Model.UserModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
public class FluxAndMonoController {

    @GetMapping(value = "/flux",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Flux<String> getFluxElement(){
        return Flux.just("hi", "there").delayElements(Duration.ofSeconds(1)).log();
    }

    @GetMapping(value = "/flux/int")
    public Flux<Integer> getIntFlux(){
        return Flux.just(1,2,3,4).log();
    }
    @GetMapping(value = "/flux/user",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<UserModel> getFluxUser(){
        return Flux.just(

                        UserModel.builder().username("Darshan").email("darshan@gmail.com").build(),
                        UserModel.builder().username("Supriya").email("supram@gmail.com").build()

        )
                .delayElements(Duration.ofSeconds(2))
                .log();
    }
    @GetMapping("/flux/duration")
    public Flux<String> getFluxElements(){

        return Flux.just("Hi", "this", "is","Flux","demo")
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping(value = "/flux/stream",produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<String> getFluxStream(){
        return Flux.just("Hi", "this", "is","Flux","demo")
                .delayElements(Duration.ofSeconds(1))
                .log();
    }

    @GetMapping("/flux/inf")
    public Flux<Long> getFluxLongInf(){
        return Flux.interval(Duration.ofSeconds(1)).log();
    }

    @GetMapping("/mono")
    public Mono<UserModel> getMonoUser(){
        return Mono.just(
                UserModel.builder().username("Ashwin").email("ashwin@gmail.com").build()
        ).log();
    }
}
