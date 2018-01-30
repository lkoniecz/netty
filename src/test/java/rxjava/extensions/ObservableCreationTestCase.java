package rxjava.extensions;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.Subscriptions;
import rxjava.utils.RxTestUtils;
import rxjava.utils.Tweet;
import rxjava.utils.WatchableObserver;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rxjava.utils.RxTestUtils.log;

public class ObservableCreationTestCase {

    @Test
    public void observableJustShouldCallOnNextOnce() {
        WatchableObserver<Tweet> observer = new WatchableObserver<>();
        Observable<Tweet> observable = Observable.just(new Tweet("siema"));
        observable.subscribe(observer);
        assertEquals(1, observer.getOnNextCounter());
    }

    @Test
    public void observableJustShouldCallOnCompletedOnce() {
        WatchableObserver<Tweet> observer = new WatchableObserver<>();
        Observable<Tweet> observable = Observable.just(new Tweet("siema"));
        observable.subscribe(observer);
        assertTrue(observer.isOnCompletedCalled());
    }

    @Test
    public void observableRangeShouldCallOnNextCorrectTimes() {
        WatchableObserver<Integer> observer = new WatchableObserver<>();
        List<Integer> values = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        Observable<Integer> observable = Observable.from(values);
        observable.subscribe(observer);
        assertEquals(10, observer.getOnNextCounter());
    }

    @Test
    public void mimicObservableJust() {
        Observable<Tweet> observable = Observable.create(subscriber -> {
            subscriber.onNext(new Tweet("siema"));
            subscriber.onCompleted();
        });

        WatchableObserver<Tweet> observer = new WatchableObserver<>();
        observable.subscribe(observer);
        assertEquals(1, observer.getOnNextCounter());
    }

    @Test
    public void subscribeToObservableWithOnNextAndOnErrorCallback() {
        Observable<Tweet> observable = Observable.just(new Tweet("siema"));
        observable.subscribe(
                System.out::println,
                Throwable::printStackTrace
        );
    }

    @Test
    public void cacheShouldNotCallCreateBodyMoreThanOnce() {
        final AtomicInteger createCount = new AtomicInteger(0);
        Observable<Integer> ints = Observable.create(subscriber -> {
                    RxTestUtils.log("create");
                    createCount.incrementAndGet();
                    subscriber.onNext(42);
                    subscriber.onCompleted();
                }
        );

        ints = ints.cache();
        RxTestUtils.log("starting");
        ints.subscribe(i -> log("Element A: " + i));
        ints.subscribe(i -> log("Element B: " + i));
        RxTestUtils.log("exit");
        assertEquals(1, createCount.get());
    }

    @Test
    public void infiniteStreamShouldBlockSubscribingThread() {
        final BigInteger ZERO = new BigInteger("0");
        final BigInteger ONE = new BigInteger("1");
        //BROKEN! Don't do this
        Observable<BigInteger> naturalNumbers = Observable.create(
                subscriber -> {
                    BigInteger i = ZERO;
                    while (true) { //don't do this!
                        subscriber.onNext(i);
                        i = i.add(ONE);
                        if (i.intValue() == 10000) {
                            break;
                        }
                    }
                });
        naturalNumbers.subscribe(x -> RxTestUtils.log(x));
    }

    @Test
    public void unsubscribeFromObservableShouldInterruptObservableThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicInteger interruptedCounter = new AtomicInteger(0); // 0 - not interrupted, 1 - interrupted
        Observable<Integer> delayedObservable = Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        try {
                            Thread.sleep(10000);
                            if (!subscriber.isUnsubscribed()) {
                                subscriber.onNext(10);
                                subscriber.onCompleted();
                            }
                        } catch (InterruptedException e) {
                            subscriber.onError(e);
                        }

                    };
                    final Thread thread = new Thread(r);
                    thread.start();
                    subscriber.add(Subscriptions.create(thread::interrupt));
                });

        Subscription sub = delayedObservable.subscribe(
                System.out::println,
                e -> {
                    interruptedCounter.incrementAndGet();
                    latch.countDown();
                }
        );
        assertEquals(0, interruptedCounter.get());
        sub.unsubscribe();
        latch.await();
        assertEquals(1, interruptedCounter.get());
    }

    @Test
    public void timesShouldDelayEmittingValues() throws InterruptedException {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribe((Long zero) -> log(zero));
        Thread.sleep(2000);
    }

    @Test
    public void testDupa() {
        Observable<String> observable = Observable.create(subscriber -> {
           subscriber.onNext("huj dupa");
           subscriber.onError(new Exception("jeeebut"));
        });
        observable.subscribe(str -> System.out.println(str), error -> System.out.println(error));
    }
}
