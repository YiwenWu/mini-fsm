package com.github.minifsm;

import com.google.common.base.Stopwatch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class ReactorTest {

    @Test
    public void testMap()  {
        Stopwatch watch = Stopwatch.createStarted();
        Flux.just(1, 2, 3, 4)
                .map(i -> {
                    sleep(1000);
                    return i;
                })
                .subscribe(System.out::println);
        assert watch.elapsed(TimeUnit.MILLISECONDS) > 4000;
    }

    @Test
    public void testMap2() {
        Stopwatch watch = Stopwatch.createStarted();
        Flux.just(1, 2, 3, 4)
                .flatMap(i -> {
                    Flux<Integer> just = Flux.just(i).flatMap(j -> {
                        sleep(1000);
                        return Flux.just(j + 1);
                    }).subscribeOn(Schedulers.parallel());
                    return just;
                }).subscribe();
        assert watch.elapsed(TimeUnit.MILLISECONDS) < 1000;
    }

    @Test
    public void testMap3() {
        Stopwatch watch = Stopwatch.createStarted();
        List<Integer> result = new ArrayList<>();
        Flux.just(1, 2, 3, 4)
                .flatMap(i -> {
                    Flux<Integer> just = Flux.just(i).flatMap(j -> {
                        sleep(1000);
                        return Flux.just(j + 1);
                    });
                    return just;
                }).subscribe(result::add);

        assert result.size() == 4;
        assert watch.elapsed(TimeUnit.MILLISECONDS) > 4000;
    }


    @Test
    public void testMap4() {
        Stopwatch watch = Stopwatch.createStarted();
        List<Integer> result = new ArrayList<>();
        Flux<Integer> flux = Flux.just(1, 2, 3, 4)
                .flatMap(i -> {
                    return Mono.defer((Supplier<Mono<Integer>>) () -> {
                        sleep(1000);
                        return Mono.just(i);
                    }).subscribeOn(Schedulers.parallel());
                });
        flux.subscribe(result::add);
        assert result.size() == 0;
        assert watch.elapsed(TimeUnit.MILLISECONDS) < 1000;
        sleep(5000);
        assert result.size() == 4;
    }


    @Test
    public void testMap5() {
        Scheduler scheduler = Schedulers.newParallel("thread-xxx", 5);
        Stopwatch watch = Stopwatch.createStarted();
        Flux<Integer> flux = Flux.just(1, 2, 3, 4)
                .flatMap(i -> {
                    Mono<Integer> mono = Mono.defer((Supplier<Mono<Integer>>) () -> {
                        Stopwatch stopwatch = Stopwatch.createStarted();
                        System.out.println("======      " + i);
                        sleep(1000);
                        if (i == 1) {
                            throw new RuntimeException("1 error");
                        }
                        long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                        System.out.println("timeElapsed=" + timeElapsed);
                        return Mono.just(i);
                    }).subscribeOn(scheduler);
                    return mono.onErrorResume(new Function<Throwable, Mono<? extends Integer>>() {
                        @Override
                        public Mono<? extends Integer> apply(Throwable throwable) {
                            System.out.println("apply+" + throwable.getMessage());
                            return Mono.just(i);
                        }
                    });
                });

        Mono<List<Integer>> mono = flux.collectList();
        List<Integer> result = Objects.requireNonNull(mono.block());
        System.out.println(result);
        assert result.size() == 4;
        long duration = watch.elapsed(TimeUnit.MILLISECONDS);
        assert duration > 1000 && duration < 4000;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {

        }
    }

}
