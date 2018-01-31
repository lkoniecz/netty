package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.ValueHolder;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static rx.Observable.interval;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartWithTestCase {

    @Test
    void startWithShouldEmitEventBeforeEmittingOriginalEvents() throws InterruptedException {
        ValueHolder<String, String> holder = ValueHolder.newInstance();
        Observable<String> obs = interval(10, MILLISECONDS)
                .map(x -> "F" + x)
                .delay(100, MILLISECONDS)
                .startWith("FX");

        obs.subscribe(event -> {
            System.out.println(event);
            if (holder.getFirstValue() == null) {
                holder.setFirstValue(event);
            }
        });

        Thread.sleep(1000);

        assertEquals("FX", holder.getFirstValue());
    }
}
