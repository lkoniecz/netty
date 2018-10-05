package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;

import java.util.Random;

public class DistinctTestCase {

    @Test
    public void distinctTestCase() {
        Observable<Integer> randomInts = Observable.create(subscriber -> {
            Random random = new Random();
            while (!subscriber.isUnsubscribed()) {
                subscriber.onNext(random.nextInt(1000));
            }
        });

        Observable<Integer> uniqueRandomInts = randomInts
                .distinct()
                .take(10);

        uniqueRandomInts.subscribe(System.out::println);
    }

    @Test
    public void distinctUntilChangedTestCase() {
        Observable.just(1, 1, 2, 3, 3, 1, 1, 4, 5, 6)
                .distinctUntilChanged()
                .subscribe(System.out::println);
    }
}
