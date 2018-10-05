package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.atomic.AtomicReference;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScanTestCase {
    @Test
    public void scanTestCase() {
        AtomicReference<Integer> currentValue = new AtomicReference<>();

        Observable
                .just(10, 32, 5, 6, 11, 12, 100)
                .scan((last, current) -> last + current)
                .toBlocking()
                .subscribe(event -> {
                    RxTestUtils.log(event);
                    currentValue.set(event);
                });

        assertTrue(currentValue.get() == 176);
    }
}
