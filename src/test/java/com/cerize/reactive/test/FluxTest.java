package com.cerize.reactive.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
@Slf4j
public class FluxTest {

    @Test
    public void fluxSubscriber(){
        Flux<String> fluxString = Flux.just("Willian", "Suane", "DevDoje", "Acedemy")
                .log();

        StepVerifier.create(fluxString).expectNext()
                .expectNext("Willian", "Suane", "DevDoje", "Acedemy")
                .verifyComplete();
    }

    @Test
    public void fluxSubscriberNumbers(){
        Flux<Integer> flux = Flux.range(1,5)
                .log();

        flux.subscribe(i-> log.info("Numeros: {}",i));

        StepVerifier.create(flux).expectNext()
                .expectNext(1,2,3,4,5)
                .verifyComplete();
    }

}
