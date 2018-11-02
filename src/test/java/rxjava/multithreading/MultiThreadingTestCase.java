package rxjava.multithreading;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rxjava.utils.RxTestUtils;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MultiThreadingTestCase {
    private static Scheduler schedulerA;
    private static Scheduler schedulerB;
    private static Scheduler schedulerC;

    private static ThreadFactory threadFactory(String pattern) {
        return new ThreadFactoryBuilder()
                .setNameFormat(pattern)
                .build();
    }

    private void log(String message) {
        RxTestUtils.log(message);
    }

    @BeforeAll
    public static void setUp() {
        ExecutorService poolA = Executors.newFixedThreadPool(10, threadFactory("Sched-A-%d"));
        schedulerA = Schedulers.from(poolA);
        ExecutorService poolB = Executors.newFixedThreadPool(10, threadFactory("Sched-B-%d"));
        schedulerB = Schedulers.from(poolB);
        ExecutorService poolC = Executors.newFixedThreadPool(10, threadFactory("Sched-C-%d"));
        schedulerC = Schedulers.from(poolC);
    }

    private Observable<String> simple() {
        return Observable.create(subscriber -> {
            RxTestUtils.log("Subscribed");
            subscriber.onNext("A");
            subscriber.onNext("B");
            subscriber.onCompleted();
        });
    }

    @Test
    public void multiThreadingBlockingTest() {
        RxTestUtils.log("Starting");
        final Observable<String> obs = simple();
        RxTestUtils.log("Created");
        final Observable<String> obs2 = obs
                .map(x -> x)
                .filter(x -> true);
        RxTestUtils.log("Transformed");
        obs2.subscribe(
                x -> RxTestUtils.log("Got " + x),
                Throwable::printStackTrace,
                () -> RxTestUtils.log("Completed")
        );
        RxTestUtils.log("Exiting");
    }

    @Test
    public void subscribeOnTest() {
        RxTestUtils.log("Starting");
        final Observable<String> obs = simple();
        RxTestUtils.log("Created");
        obs
                .subscribeOn(Schedulers.io())
                .subscribe(
                        x -> RxTestUtils.log("Got " + x),
                        Throwable::printStackTrace,
                        () -> RxTestUtils.log("Completed")
                );
        RxTestUtils.log("Exiting");
    }

    @Test
    public void gupiTest() {
        final Observable<String> obs = simple();

        obs
                .doOnNext(s -> RxTestUtils.log("DoOnNext: " + s))
                .doOnCompleted(() -> RxTestUtils.log("doOnCompleted"))
                .subscribe(this::log);

        //obs.subscribe(this::log);
    }

    @Test
    public void doOnNextTest() {
        log("Starting");
        final Observable<String> obs = simple();
        log("Created");
        obs
                .doOnNext(this::log)
                .map(x -> x + '1')
                .doOnNext(this::log)
                .map(x -> x + '2')
                .subscribeOn(schedulerA)
                .doOnNext(this::log)
                .subscribe(
                        x -> log("Got " + x),
                        Throwable::printStackTrace,
                        () -> log("Completed")
                );
        log("Exiting");
    }

    @Test
    public void testGroceries() {
        RxGroceries rxGroceries = new RxGroceries();
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .subscribeOn(schedulerA) //BROKEN!!!
                .map(prod -> rxGroceries.doPurchase(prod, 1))
                .reduce(BigDecimal::add)
                .single();

        totalPrice
                .toBlocking()
                .subscribe(event -> log(event.toString()));
    }

    @Test
    public void flatMapTest() {
        RxGroceries rxGroceries = new RxGroceries();
        Observable<BigDecimal> totalPrice = Observable
                .just("bread", "butter", "milk", "tomato", "cheese")
                .flatMap(prod ->
                        rxGroceries
                                .purchase(prod, 1)
                                .subscribeOn(schedulerA))
                .reduce(BigDecimal::add)
                .single();

        totalPrice
                .toBlocking()
                .subscribe(event -> log(event.toString()));
    }

}
