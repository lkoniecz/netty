package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static rx.Observable.timer;

public class OperatorsTestCase {
    @Test
    public void delayMethodShouldDelayEmittingAnEventStartingFromTheBeginning() throws InterruptedException {
        List<String> words = Arrays.asList("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit");
        CountDownLatch latch = new CountDownLatch(words.size());

        List<String> orderedWords = new ArrayList<>();

        // This will emit sit on 3rd second, amet and elit on 4th and so on.
        Observable
                .from(words)
                .delay(word -> timer(word.length(), TimeUnit.SECONDS))
                .subscribe(word -> {
                    System.out.println(word);
                    latch.countDown();
                    orderedWords.add(word);
                });

        latch.await();
        assertEquals(3, orderedWords.get(0).length());
        assertEquals(4, orderedWords.get(1).length());
        assertEquals(4, orderedWords.get(2).length());
        assertEquals(5, orderedWords.get(3).length());
        assertEquals(5, orderedWords.get(4).length());
        assertEquals(5, orderedWords.get(5).length());
        assertEquals(8, orderedWords.get(6).length());
        assertEquals(10, orderedWords.get(7).length());
    }

    @Test
    public void compactMapShouldPreserveOrderFromUpstream() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        Observable<String> obs = Observable
                .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .concatMap(dow -> {
                    switch(dow) {
                        case SUNDAY:
                            return Observable
                                    .interval(90, MILLISECONDS)
                                    .take(5)
                                    .map(i -> {
                                        latch.countDown();
                                        return "Sun-" + i;
                                    });
                        case MONDAY:
                            return Observable
                                    .interval(65, MILLISECONDS)
                                    .take(5)
                                    .map(i -> {
                                        latch.countDown();
                                        return "Mon-" + i;
                                    });
                        default:
                            return null;
                    }
                });

        obs.subscribe(onNext -> System.out.println("onNext: " + onNext),
                onError -> System.out.println("Error: " + onError),
                () -> System.out.println("Completed"));
        latch.await();
    }

    @Test
    public void someTest() {
        Observable
                .just(8, 9, 10)
                .doOnNext(i -> System.out.println("A: " + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: " + i))
                .map(i -> "#" + i * 10)
                .doOnNext(s -> System.out.println("C: " + s))
                .filter(s -> s.length() < 4)
                .subscribe(s -> System.out.println("D: " + s));
    }
}
