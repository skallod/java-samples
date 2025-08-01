package ru.galuzin.tasks.reactive;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.LongStream;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.junit.jupiter.api.Assertions.*;

class ArrayPublisherTest extends PublisherVerification<Long> {

    public ArrayPublisherTest() {
        super(new TestEnvironment());
    }

    @Test
    public void signalsInTheRightOrder() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Long> collected = new ArrayList<>();
        ArrayList<Integer> order = new ArrayList<>();
        long requests = 5L;
        Long[] array = generate(requests);
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);
        publisher.subscribe(new Subscriber<Long>() {

            @Override
            public void onSubscribe(Subscription subscription) {
                order.add(0);
                subscription.request(requests);
            }

            @Override
            public void onNext(Long aLong) {
                collected.add(aLong);
                if (!order.contains(1)) {
                    order.add(1);
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {
                order.add(2);
                latch.countDown();
            }
        });

        latch.await(1, TimeUnit.SECONDS);
        assertEquals(order, List.of(0, 1, 2));
        assertEquals(Arrays.asList(array), collected);
    }

    @Test
    public void supportBackprassure() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Long> collected = new ArrayList<>();
        long requests = 5L;
        Long[] array = generate(requests);
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);
        Subscription[] subscription = new Subscription[1];
        System.out.println("Before Thread.currentThread().getId() = " + Thread.currentThread().getId());
        System.out.println("Before Thread.currentThread().getName() = " + Thread.currentThread().getName());


        publisher.subscribe(new Subscriber<Long>() {

            @Override
            public void onSubscribe(Subscription s) {
                subscription[0] = s;
            }

            @Override
            public void onNext(Long aLong) {
                System.out.println("Thread.currentThread().getId() = " + Thread.currentThread().getId());
                System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                collected.add(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });

        assertEquals(collected, Collections.emptyList());
        subscription[0].request(1);
        assertEquals(collected, List.of(0L));
        subscription[0].request(1);
        assertEquals(collected, List.of(0L, 1L));
        subscription[0].request(2);
        assertEquals(collected, List.of(0L, 1L, 2L, 3L));
        subscription[0].request(20);

        latch.await(1, TimeUnit.SECONDS);
        assertEquals(Arrays.asList(array), collected);
        System.out.println("Backprassure test finish");
    }

    @Test
    public void sendNpeInOnErrorBlock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Long[] array = new Long[]{null};
        AtomicReference<Throwable> error = new AtomicReference<>();
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);

        publisher.subscribe(new Subscriber<Long>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(3);
            }

            @Override
            public void onNext(Long aLong) {
            }

            @Override
            public void onError(Throwable throwable) {
                error.set(throwable);
                latch.countDown();
            }

            @Override
            public void onComplete() {
            }
        });
        latch.await(1, TimeUnit.SECONDS);
        assertTrue(error.get() instanceof NullPointerException, "NPE has been got.");

    }


    @Test
    public void stackOverflow() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Long> collected = new ArrayList<>();
        long requests = 1000L;
        Long[] array = generate(requests);
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);

        publisher.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                s.request(1);
            }

            @Override
            public void onNext(Long aLong) {
                collected.add(aLong);
                s.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertEquals(Arrays.asList(array), collected);
        System.out.println("Overflow test finish");
    }

    @Test
    public void testCancel() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Long> collected = new ArrayList<>();
        long toRequest = 1000L;
        Long[] array = generate(toRequest);
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);

        publisher.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                s.cancel();
                s.request(toRequest);
            }

            @Override
            public void onNext(Long aLong) {
                collected.add(aLong);
                s.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertEquals(0, collected.size());
        System.out.println("Overflow test finish");
    }

    @Test
    public void testMultithreading() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ArrayList<Long> collected = new ArrayList<>();
        long toRequest = 5000L;
        Long[] array = generate(toRequest);
        ArrayPublisher<Long> publisher = new ArrayPublisher<>(array);

        publisher.subscribe(new Subscriber<Long>() {
            Subscription s;

            @Override
            public void onSubscribe(Subscription s) {
                this.s = s;
                for (int i = 0; i < toRequest; i++) {
                    commonPool().execute(() -> s.request(1));
                }
            }

            @Override
            public void onNext(Long aLong) {
                collected.add(aLong);
            }

            @Override
            public void onError(Throwable throwable) {
            }

            @Override
            public void onComplete() {
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        assertEquals(array.length, collected.size());
        assertEquals(Arrays.asList(array), collected);
        System.out.println("Overflow test finish");
    }

    private Long[] generate(long num) {
        return LongStream.range(0, num >= Integer.MAX_VALUE ? 1_000_000 : num)
                .boxed()
                .toArray(Long[]::new);
    }

    @Override
    public Publisher<Long> createPublisher(long l) {
        return new ArrayPublisher<>(generate(l));
    }

    @Override
    public Publisher<Long> createFailedPublisher() {
        return null;
    }
}