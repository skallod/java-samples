package ru.galuzin.tasks.reactive;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Написать свай реактивный массив публикатор.
 * @param <T>
 */
public class ArrayPublisher<T> implements Publisher<T> {

    private final T[] array;

    public ArrayPublisher(T[] array) {
        this.array = array;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscribe(new Subscription() {

            private volatile boolean canceled;

            private AtomicInteger index = new AtomicInteger(0);

            AtomicLong requested = new AtomicLong(0);

            private volatile boolean completedHasBeenSent;

            @Override
            public synchronized void request(long n) {

                if (n <= 0 && !canceled) {
                    cancel();
                    subscriber.onError(new IllegalArgumentException("Requested " + n + " elements, which must be positive"));
                }

                // Паттерн in-progress
                long initialRequested = requested.getAndAdd(n); // добавили элементов в обработку
                if (initialRequested > 0) {
                    // обработка уже выполняется (не всегда), выходим
                    return;
                }
                // --
                while (true) {
                    int sent = 0;
                    for (; sent < requested.get() && index.get() < array.length; sent++, index.incrementAndGet()) {
                        if (canceled) {
                            return;
                        }
                        T item = array[index.get()];
                        if (item == null) {
                            subscriber.onError(new NullPointerException());
                            return;
                        }
                        subscriber.onNext(item);
                    }
                    if (canceled) {
                        return;
                    }
                    if (index.get() == array.length && !completedHasBeenSent) {
                        subscriber.onComplete();
                        completedHasBeenSent = true;
                    }
                    if (requested.addAndGet(-sent) == 0) {
                        return;
                    }
                    // repeat остались не обработанные элементы
                }
            }

            @Override
            public void cancel() {
                canceled = true;
            }
        });
    }
}
