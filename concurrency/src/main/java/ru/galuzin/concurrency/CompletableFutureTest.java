package ru.galuzin.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class CompletableFutureTest {

    private static final Logger log = LoggerFactory.getLogger(CompletableFutureTest.class);

    private final ConcurrentHashMap<String,CompletableFuture<Boolean>> ownerLagTaskFuture = new ConcurrentHashMap<>();

//    private static final ConcurrentHashMap<String, AtomicBoolean> ownerTransitTaskFlag = new ConcurrentHashMap<>();

    ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        CompletableFutureTest futureTest = new CompletableFutureTest();
        //async
//        futureTest.cancelProcess("to", 3);// not work
//        CompletableFuture.runAsync(()->futureTest.initProcess("to", TestCase.TIME_OUT),futureTest.executorService);
//        CompletableFuture.runAsync(()->futureTest.initProcess("to", TestCase.TIME_OUT),futureTest.executorService);
        futureTest.test2();
        Thread.sleep(180_000);
    }



    enum TestCase { INNER_E, TIME_OUT, SUCCESS;}
    void initProcess(String owner, TestCase testCase){
        // Call this::convert method reference when both
        // previous stages complete.
//        CompletableFuture<String> feature = CompletableFuture.supplyAsync(() -> {
//            log.info("supply async");
//            return "hello";
//        });
        log.info("transit start {}", owner);
        AtomicBoolean transitTaskFlag = new AtomicBoolean(true);
//        AtomicBoolean prevBool = ownerTransitTaskFlag.putIfAbsent(owner, transitTaskFlag);
//        if(prevBool != null) {
//            String mes = "Задача перехода уже выполняется";
//            return;
//        }
        CompletableFuture<Boolean> future =
                new CompletableFuture<>();
        //future.com
        CompletableFuture<Boolean> prevF = ownerLagTaskFuture.putIfAbsent( owner , //(k, v) -> {
//            if (v == null || v.isDone()) {
//                return
                future
//            } else {
//                log.error("exist feature {}",owner);
//                return v;
//            }
//        }
        );
        if(prevF != null) {
            String mes = "Задача перехода уже выполняется";
            log.warn("Transit task already process");
            return;
        }

        startLagTask(future, testCase, transitTaskFlag, owner);

        //.handle((r,t)->{})
        // Swallow the exception.
//        .exceptionally(ex -> null)
        String message = null;
        try {
            future
                    // Block until all async processing completes.
                    .join();
        } catch (CancellationException cancellationE) {
            log.warn("cancellation e", cancellationE);
            message = "Задача отменена";
            //флаг фалсе
        } catch (CompletionException completionE) {
            message = "Ошибка в ходе получения лага " + completionE.getMessage();
            log.error(message, completionE);
        } catch (Throwable e) {
            log.error("Неизвестная ошибка",e);
            message = "Неизвестная ошбика";
            //флаг фалсе
        }

//        log.info("is done {}" , transitTaskFeature.isDone());

        if (message!=null) {
            //set prev state
        } else {
            //set new state
        }

        transitTaskFlag.set(false);
        ownerLagTaskFuture.remove(owner);
//        ownerTransitTaskFlag.remove(owner);

        if( message!=null ) {
            throw new IllegalStateException(message);
        }

        log.info("finish");

    }

    private void cancelProcess(String owner, int sleepCount) {
        CompletableFuture.runAsync(()-> {
            for (int i = 0; i < sleepCount; i++) {
                sleep();
            }
            CompletableFuture<Boolean> future = ownerLagTaskFuture.get(owner);
            log.info("cancel");
            future.cancel(true);
        }
        );
//        CompletableFuture.get(()->{
//            sleep();
//            sleep();
//            workerFeature.cancel(true);
//        });
    }

    private void startLagTask(CompletableFuture<Boolean> future, TestCase testCase, AtomicBoolean transitTaskFlag, String owner) {
        log.info("new feature {}", owner);
        future.completeAsync(() -> {
                log.info("supply async");
                do {
                    log.info("iteration "+ Thread.currentThread().isInterrupted());
                    int i = longOper();
                    if( testCase==TestCase.TIME_OUT ) {
                        i = i+1;
                    }
                    if (i == 0) {
                        return true;
                    }
                    sleep();
                    if ( testCase==TestCase.INNER_E ) {
                        i = i/(i-1);
                    }
                } while (transitTaskFlag.get());
                return false;
                //TODO replace with await
            },executorService)
            // If async processing takes more than 3 seconds a
            // TimeoutException will be thrown.
            .orTimeout(5, TimeUnit.SECONDS)
            // This method always gets called, regardless of
            // whether an exception occurred or not.
            .whenComplete((result, ex) -> {
                if (result != null) {
                    log.info("Completion with result {}", result);
                } else if (ex != null) {
                    log.error("The exception thrown was", ex);
                }
            });
    }

    private void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            log.info("**** interapterd");
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private int lagInfo() {
        return new Random().nextInt(30);
    }

    int longOper() {
        long timeMillis = System.nanoTime();
        double result= 0;
        for(int i=0; i < 10_000_000 ; i++) {
            result+= Math.log(Math.pow(Math.log(Math.pow(Math.log(Math.log(Math.log(Math.pow(Math.log(Math.log(Math.sqrt(Math.log(Math.random()*Math.PI)))),2d)))),2d)),2d));
        }
        log.info("op sec = " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - timeMillis));
        return (int)result;
    }

    private void test2() {
        for (int i = 0; i < 2; i++) {
            final CompletableFuture<String> stringCompletableFuture = _test2();
            final String join = stringCompletableFuture.join();
            System.out.println("join = " + join);
        }
    }
    private CompletableFuture<String> _test2() {
        return submit(() -> {
            log.info("supplier start");
            final String value1 = MDC.get("key1");
            log.info("value1 = " + value1);
            MDC.put("key1", UUID.randomUUID().toString());
            log.info("supplier finish");
            return "Mega result";
        })
            .whenComplete((u, ex) -> {
                log.info("Chain 3 req {} {}", u, MDC.get("key1"));
                if (ex != null) {
                    log.warn("Chain 3 ex", ex);
                }});
    }

    <U> CompletableFuture<U> submit(Supplier<U> supplier) {
        return CompletableFuture.supplyAsync(supplier)
            .whenComplete((u, ex) -> {
            log.info("Chain 2 req {}", u);
            if (ex != null) {
                log.warn("Chain 2 ex", ex);
            }
        });
    }

    void operTimeout() {

    }

    void operCancel() {

    }

    void operSuccess() {

    }

    void operInternalException() {

    }
}
