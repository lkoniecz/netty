package rxjava.timerandinterval;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.TimeUnit;

public class IntervalTestCase {

    @Test
    public void intervalTest() throws InterruptedException {
        Observable.interval(1_000_000 / 60, TimeUnit.MICROSECONDS).subscribe((Long i) -> RxTestUtils.log(i));
        Thread.sleep(1000);
    }
}
