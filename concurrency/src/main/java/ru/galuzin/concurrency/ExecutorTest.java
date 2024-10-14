package ru.galuzin.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@Slf4j
public class ExecutorTest {

    public static void main(String[] args) throws Exception {
        final ExecutorService executorService = defaultExecutorService();
        try {
            CompletableFuture.runAsync(() -> {
                try {
                    Thread.sleep(60000);
                    log.info("finish task1");
                } catch (InterruptedException e) {
                    log.error("Sleep", e);
                }
            }, executorService).get(10000L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
           log.error("first task", e);
        }
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10000);
                log.info("finish task2");
            } catch (InterruptedException e) {
                log.error("Sleep", e);
            }
        }, executorService).get(11000L, TimeUnit.MILLISECONDS);
        log.info("finish main");
//        try {
//            submitted1.get();
//            submitted2.get();
//        } catch (Exception e) {
//            log.error("Catch block", e);
//        }
        Thread.sleep(60000);
    }

    private static ExecutorService defaultExecutorService() {
        return new ThreadPoolExecutor(1, 1,
            60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new BasicThreadFactory.Builder()
                .namingPattern("app-parse-query-%d")
                .daemon(true)
                .build(),
            (r, executor) -> {
                String errorStr = "[APP] AppConfig.parseStatementExecutorService reject task.";
                log.error(errorStr + " {}, {}", r, executor);
                throw new RuntimeException(errorStr);
            }
        );
    }
}
