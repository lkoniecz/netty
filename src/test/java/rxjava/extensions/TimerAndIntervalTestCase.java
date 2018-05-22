package rxjava.extensions;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.TimeUnit;

public class TimerAndIntervalTestCase {

    @Test
    public void testTimer() {
        Observable
                .timer(1, TimeUnit.SECONDS)
                .subscribe((Long zero) -> RxTestUtils.log(zero));

        RxTestUtils.log("przed sleep");
        RxTestUtils.sleep(2, TimeUnit.SECONDS);
        RxTestUtils.log("po sleep");
    }

    @Test
    public void testInterval() {
        Observable
                .interval(100, TimeUnit.MILLISECONDS)
                .subscribe((Long i) -> RxTestUtils.log(i));

        RxTestUtils.sleep(2, TimeUnit.SECONDS);
    }
}
