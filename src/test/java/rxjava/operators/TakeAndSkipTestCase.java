package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func1;


public class TakeAndSkipTestCase {

    @Test
    public void takeTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe"))
                .take(3)
                .subscribe(System.out::println);
    }

    @Test
    public void takeLastTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe"))
                .takeLast(3)
                .subscribe(System.out::println);
    }

    @Test
    public void skipTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe"))
                .skip(3)
                .subscribe(System.out::println);
    }

    @Test
    public void skipLastTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe"))
                .skipLast(3)
                .subscribe(System.out::println);
    }

    @Test
    public void takeFirstTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe"))
                .takeFirst(integer -> integer == 5)
                .subscribe(System.out::println);
    }

    //takeUntil

    //takeWhile

    //elementAt

    //count

    //all(predicate), exists(predicate), and contains(value)


    @Test
    public void allTestCase() {
        Observable.just(1, 2, 3, 5, 1, 2, 3, 4, 5, 1)
                .all(integer -> integer < 10)
                .subscribe(System.out::println);
    }
}
