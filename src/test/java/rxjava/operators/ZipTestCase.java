package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Zipping is the act of taking two (or more) streams and combining them with each
 * other in such a way that each element from one stream is paired with corresponding
 * event from the other.
 */
public class ZipTestCase {

    @Test
    public void zipWithShouldZipValuesOneByOne() {
        Observable<String> firstNamesObs = Observable.just("łukasz", "jan");
        Observable<String> lastNamesObs = Observable.just("konieczny", "kowalski");

        Observable<String> fullNames = firstNamesObs.zipWith(lastNamesObs, new Func2<String, String, String>() {

            @Override
            public String call(String s, String s2) {
                return s.concat(" ").concat(s2);
            }
        });

        List<String> fullNamesList = new ArrayList<>();
        fullNames.subscribe(event -> {
            fullNamesList.add(event);
        });

        assertEquals("łukasz konieczny", fullNamesList.get(0));
        assertEquals("jan kowalski", fullNamesList.get(1));
    }

    @Test
    void zipWithShouldNotEmitNotPairedEvents() {
        Observable<String> firstNamesObs = Observable.just("łukasz", "jan", "rychu");
        Observable<String> lastNamesObs = Observable.just("konieczny", "kowalski");

        Observable<String> fullNames = firstNamesObs.zipWith(lastNamesObs, (s, s2) -> s.concat(" ").concat(s2));

        List<String> fullNamesList = new ArrayList<>();
        fullNames.subscribe(event -> fullNamesList.add(event));

        assertEquals(2, fullNamesList.size());
    }

    @Test
    public void zippedObservableShouldWaitForTheOtherToEmitValue() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Observable<Integer> zippedObs = Observable
                .just(69)
                .zipWith(Observable.just(1).delay(2, TimeUnit.SECONDS), (first, second) ->  {
                    latch.countDown();
                    return first + second;
                });

        zippedObs.subscribe(event -> assertEquals(70, event.intValue()));
        latch.await();
    }
}
