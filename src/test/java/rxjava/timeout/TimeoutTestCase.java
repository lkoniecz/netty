package rxjava.timeout;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.TimeUnit;


public class TimeoutTestCase {

    @Test
    public void timeoutTestCase() {
        final Observable<Integer> delay1 = Observable
                .range(1, 20)
                .delay(1000, TimeUnit.MILLISECONDS);

        final Observable<Integer> delay2 = Observable
                .range(30, 20)
                .delay(2000, TimeUnit.MILLISECONDS);

        delay1
                .concatWith(delay2)
                .timeout(1800, TimeUnit.MILLISECONDS)
                .toBlocking()
                .subscribe(RxTestUtils::log, RxTestUtils::log);
    }

    @Test
    public void testBasics() {
        Observable
                .just("wal sie")
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe(RxTestUtils::log);

        Observable<String> just = Observable
                .range(1, 20)
                .map(item -> "Wal sie: " + item / 0);
            //    .retry(); // ??

        just.subscribe(RxTestUtils::log);
    }
}
