package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheTestCase {

    @Test
    public void cacheTestCase() {
        AtomicInteger counter = new AtomicInteger(0);
        Observable<Integer> obs = Observable.<Integer>create(subscriber -> {
            Integer rc = heavyQuery(counter);
            subscriber.onNext(rc);
            subscriber.onCompleted();
        }).cache();

        obs.subscribe(i -> RxTestUtils.log("Subscriber A: " + i));
        obs.subscribe(i -> RxTestUtils.log("Subscriber B: " + i));

        assertTrue(counter.get() == 1);
    }

    private Integer heavyQuery(AtomicInteger counter) {
        RxTestUtils.log("performing heave query");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RxTestUtils.log("done performing heave query");
        counter.incrementAndGet();
        return 69;
    }
}
