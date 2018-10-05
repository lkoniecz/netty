package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReduceTestCase {

    @Test
    public void reduceTestCase() {
        AtomicReference<Integer> currentValue = new AtomicReference<>();
        AtomicInteger eventsEmitted = new AtomicInteger();
        eventsEmitted.set(0);

        Observable
                .just(10, 32, 5, 6, 11, 12, 100)
                .reduce((last, current) -> last + current)
                .toBlocking()
                .subscribe(event -> {
                    RxTestUtils.log(event);
                    currentValue.set(event);
                    eventsEmitted.incrementAndGet();
                });

        assertTrue(currentValue.get() == 176);
        assertTrue(eventsEmitted.get() == 1);
    }
}
