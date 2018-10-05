package rxjava.random.shit;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Scheduler;
import rx.observables.ConnectableObservable;
import rx.schedulers.Schedulers;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.TimeUnit;


public class SomeTestCase {

    private void donOnNext(String str) {
        RxTestUtils.log(str);
    }

    @Test
    public void someTest() {
        Observable<String> obs = Observable.just("duuuuupa");
        obs.doOnNext(this::donOnNext);
        obs.subscribe(System.out::println);
    }

    @Test
    public void connectedTestCase() {
        Observable<String> obs = Observable.just("duuuuupa");
        ConnectableObservable<String> connectedObs = obs.publish();

        connectedObs.subscribe(this::donOnNext);
        connectedObs.subscribe(this::donOnNext);
        connectedObs.subscribe(this::donOnNext);
        connectedObs.subscribe(this::donOnNext);

        connectedObs.connect();
    }

    @Test
    public void zipTestCase() {
        Observable<String> strObs = Observable.fromCallable(() ->  {
            RxTestUtils.log("fetching string..");
            RxTestUtils.sleep(2, TimeUnit.SECONDS);
            return "lukasz";
        });

        Observable<Integer> intObs = Observable.fromCallable(() -> {
            RxTestUtils.log("fetching int..");
            RxTestUtils.sleep(1, TimeUnit.SECONDS);
            return 69;
        });

        strObs
                .zipWith(intObs, (String s, Integer i) -> s + " : " +  i)
                .subscribe(RxTestUtils::log);
    }

    @Test
    public void zipTestCaseAsync() {
        Observable<String> strObs = Observable.fromCallable(() ->  {
            RxTestUtils.log("fetching string..");
            RxTestUtils.sleep(2, TimeUnit.SECONDS);
            return "lukasz";
        }).subscribeOn(Schedulers.io());
        Observable<Integer> intObs = Observable.fromCallable(() -> {
            RxTestUtils.log("fetching int..");
            RxTestUtils.sleep(1, TimeUnit.SECONDS);
            return 69;
        }).subscribeOn(Schedulers.io());

        strObs
                .zipWith(intObs, (String s, Integer i) -> s + " : " +  i)
                .toBlocking()
                .subscribe(RxTestUtils::log);

    }

    @Test
    public void timeoutTestCase() {
        Observable
                .fromCallable(() -> {
                    RxTestUtils.sleep(2, TimeUnit.SECONDS);
                    return "lukasz";
                })
                .timeout(1500, TimeUnit.MILLISECONDS)
                .subscribe(RxTestUtils::log);

    }
}
