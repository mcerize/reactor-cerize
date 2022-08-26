package com.cerize.reactive.test;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
public class MonoTest {

    @Test
    public void monoSubscriber() {
        String nome = "Mateus Cerize";
        Mono<String> mono = Mono.just(nome).log();
        mono.subscribe();
        log.info("-----------------------------");
        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();

    }

    @Test
    public void monoSubscriberConsumer() {
        String nome = "Mateus Cerize";
        Mono<String> mono = Mono.just(nome).log();

        mono.subscribe(s -> log.info("Value {}", s));
        log.info("-----------------------------");


        StepVerifier.create(mono)
                .expectNext(nome)
                .verifyComplete();

    }

    @Test
    public void monoSubscriberError() {
        String nome = "Mateus Cerize";
        Mono<String> mono = Mono.just(nome)
                .map(s -> {
                    throw new RuntimeException("Teste mono error");
                });


        mono.subscribe(s -> log.info("Name {}", s), s -> log.error("teste do erro no subscribe"));
        mono.subscribe(s -> log.info("Name stacktrace {}", s), Throwable::printStackTrace);
        log.info("-----------------------------");


        StepVerifier.create(mono)
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void monoSubscriberConsumerComplete() {
        String nome = "Mateus Cerize";
        Mono<String> mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Terminou!! {}"));

        log.info("-----------------------------");


        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();

    }

    @Test
    public void monoSubscriberConsumerSubscription() {
        String nome = "Mateus Cerize";
        Mono<String> mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase);

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Terminou!! ")
                , subscription -> subscription.request(5));

        log.info("-----------------------------");


        StepVerifier.create(mono)
                .expectNext(nome.toUpperCase())
                .verifyComplete();

    }

    @Test
    public void monoDoOnMethods() {
        String nome = "Mateus Cerize";
        Mono<Object> mono = Mono.just(nome)
                .log()
                .map(String::toUpperCase)
                .doOnSubscribe(subscription -> log.info("Subscribed"))
                .doOnRequest(longNumber -> log.info("Comça a fazer alguma coisa"))
                .doOnNext(s -> log.info("Value is here. Executing doOnNext {}", s))
                .flatMap(s -> Mono.empty())
                .doOnNext(s -> log.info("22Value is here. Executing doOnNext {}", s))
                .doOnSuccess(s -> log.info("doOnSuccess executed {}", s)
                );

        mono.subscribe(s -> log.info("Value {}", s),
                Throwable::printStackTrace,
                () -> log.info("Terminou!! ")
                , subscription -> subscription.request(5));

    }

    @Test
    public void monoDoOnError() {
        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception cer"))
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .doOnNext(s -> log.info("Executando doOnnext"))
                .log();


        StepVerifier.create(error)
                .expectError(IllegalConnectorArgumentsException.class)
                .verify();
    }

    @Test
    public void monoDoOnErrorResume() {
        String name = "Cerize teste";

        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception cer"))
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .onErrorResume(s -> {
                            log.info("Ocorreu o erro e quero continuar");
                            return Mono.just(name);
                        }
                )
                .doOnNext(s -> log.info("Executando doOnnext mesmo gerando erro {}",s))
                .log();


        StepVerifier.create(error)
                .expectNext(name)
                .verifyComplete();
    }


    @Test
    public void monoDoOnErrorReturn() {
        String name = "Cerize teste";

        Mono<Object> error = Mono.error(new IllegalArgumentException("Illegal argument exception cer"))
                .doOnError(e -> MonoTest.log.error("Error message: {}", e.getMessage()))
                .onErrorResume(s -> {
                            log.info("Ocorreu o erro e quero continuar");
                            return Mono.just(name);
                        }
                )
                .doOnNext(s -> log.info("Executando doOnnext mesmo gerando erro {}",s))
                .log();


        StepVerifier.create(error)
                .expectNext(name)
                .verifyComplete();
    }
}


