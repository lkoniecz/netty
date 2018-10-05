package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Action0;
import rxjava.utils.Person.Gender;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcatTestCase {

    @Test
    public void concatTestCase() {
        List<Integer> data = IntStream
                .range(1, 200)
                .boxed()
                .collect(Collectors.toList());

        Observable<Integer> veryLong = Observable.from(data);
        final Observable<Integer> ends = Observable.concat(
                veryLong.take(5),
                veryLong.takeLast(5)
        );

        ends.subscribe(System.out::println);
    }

    @Test
    public void concatShouldNotSubscribeToSecondStreamIfFirstEmitsValue() {
        Observable<String> fromCache = Observable.just("lukasz", "konieczny");

        AtomicBoolean dbLoaded = new AtomicBoolean(false);

        Observable<String> fromDb = Observable
                .just("lukasz", "konieczny")
                .doOnSubscribe(() -> dbLoaded.set(true));

        Observable<String> found = Observable
                .concat(fromCache, fromDb)
                .first();

        found.subscribe(System.out::println);

        assertTrue(dbLoaded.get() == false);
    }
}
