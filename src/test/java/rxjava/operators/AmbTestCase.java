package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * amb() (together with ambWith()), subscribes to all upstream Observables it controls
 * and waits for the very first item emitted. When one of the Observables emits the first event,
 * amb() discards all other streams and just keep forwarding events from the first Observable that woke up.
 */
public class AmbTestCase {

    @Test
    public void ambShouldEmitEventsFromTheStreamThatEmitsEventFirst() throws InterruptedException {
        List<String> events = new ArrayList<>();
        Observable.amb(
                stream(100, 17, "S"),
                stream(200, 10, "F")
        ).subscribe(event -> {
            RxTestUtils.log(event);
            events.add(event);
        });

        Thread.sleep(2000);

        boolean allFromFirstStream = events.stream().allMatch(item -> item.startsWith("S"));
        assertTrue(allFromFirstStream);
    }

    private Observable<String> stream(int initialDelay, int interval, String name) {
        return Observable
                .interval(initialDelay, interval, MILLISECONDS)
                .map(x -> name + x)
                .doOnSubscribe(() -> System.out.println("Subscribe to " + name))
                .doOnUnsubscribe(() -> System.out.println("Unsubscribe from " + name));
    }

    @Test
    public void ambTestCaseNotSubscribing() throws InterruptedException {
        Observable<Object> obs1 = Observable
                .create(subscriber -> subscriber.onNext("obs 1 event"))
                .doOnSubscribe(() -> System.out.println("obs1 sub"))
                .doOnUnsubscribe(() -> System.out.println("obs1 unsub"));

        Observable<Object> obs2 = Observable
                .create(subscriber -> subscriber.onNext("obs 2 event"))
                .doOnSubscribe(() -> System.out.println("obs2 sub"))
                .doOnUnsubscribe(() -> System.out.println("obs2 unsub"));

        Observable
                .amb(obs1, obs2)
                .subscribe(System.out::println);

        Thread.sleep(500);
    }

    @Test
    public void ambTestCase() throws InterruptedException {
        Observable<String> obs1 = Observable
                .just("obs1")
                .doOnSubscribe(() -> System.out.println("obs1 sub"))
                .doOnUnsubscribe(() -> System.out.println("obs1 unsub"));

        Observable<String> obs2 = Observable
                .just("obs2")
                .doOnSubscribe(() -> System.out.println("obs2 sub"))
                .doOnUnsubscribe(() -> System.out.println("obs2 unsub"));

        Observable
                .amb(obs1, obs2)
                .subscribe(System.out::println);

        Thread.sleep(500);
    }
}
