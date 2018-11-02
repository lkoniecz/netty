package rxjava.backpressure;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

public class BackPressureTestCase {

    @Test
    public void sampleTestCase() {
        long startTime = System.currentTimeMillis();
        Observable
                .interval(7, MILLISECONDS)
                .timestamp()
                .sample(1, TimeUnit.SECONDS)
                .map(ts -> ts.getTimestampMillis() - startTime + "ms: " + ts.getValue())
                .take(5)
                .subscribe(System.out::println);
    }

    @Test
    public void sample2TestCase() {
        Observable<String> names = Observable
                .just("Mary", "Patricia", "Linda", "Barbara", "Elizabeth", "Jennifer", "Maria", "Susan", "Margaret", "Dorothy");

        Observable<Long> absoluteDelayMillis = Observable
                .just(0.1, 0.6, 0.9, 1.1, 3.3, 3.4, 3.5, 3.6, 4.4, 4.8)
                .map(d -> (long)(d * 1_000));

        Observable<String> delayedNames = names
                .zipWith(absoluteDelayMillis,
                        (n, d) -> Observable
                                .just(n)
                                .delay(d, MILLISECONDS))
                .flatMap(o -> o);

        delayedNames
                .sample(1, SECONDS)
                .toBlocking()
                .subscribe(System.out::println);
    }

    @Test
    public void onStartTest() {
        Observable
                .range(1, 100)
          //      .toBlocking()
                .subscribe(new Subscriber<Integer>() {


                    @Override
                    public void onCompleted() {
                        RxTestUtils.log("onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        RxTestUtils.log("onError: " + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        RxTestUtils.log("onNext: " + integer);
                    }
                });

    }

    @Test
    public void onErrorResumeNextTest() {
        Observable
                .range(1, 100)
                .map(event -> (event % 2) == 0 ? null : String.valueOf(event))
                .map(String::toString)
                .onErrorResumeNext(Observable.just("das", "das2"))
                .subscribe(RxTestUtils::log);
    }
}
