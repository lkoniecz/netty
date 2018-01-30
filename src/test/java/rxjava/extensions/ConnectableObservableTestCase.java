package rxjava.extensions;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConnectableObservableTestCase {

    /**
     * Shares one subscriber and remains lazy.
     * Same as observable.share()
     */
    @Test
    public void refCountShouldSubscribeOnlyOneSubscriber() {
        AtomicInteger connectionsEstablished = new AtomicInteger(0);
        Observable<String> observable = Observable.create(subscriber -> {
            System.out.println("Establishing connection");
            connectionsEstablished.incrementAndGet();
            subscriber.add(Subscriptions.create(() -> System.out.println("Disconnecting")));
            subscriber.onNext("Sieeeeema");
        });

        //ConnectableObservable.refCount returns Observable<T> that is connected as long as there are subscribers to it.
        observable = observable.publish().refCount();
        Subscription sub1 = observable.subscribe(System.out::println);
        System.out.println("Subscribed 1");
        Subscription sub2 = observable.subscribe(System.out::println);
        System.out.println("Subscribed 2");
        sub1.unsubscribe();
        System.out.println("Unsubscribed 1");
        sub2.unsubscribe();
        System.out.println("Unsubscribed 2");
        assertEquals(1, connectionsEstablished.get());
    }

    @Test
    public void doOnNextSubscribeShouldNotProtectFromOtherSubscribersToSubscribe() {
        AtomicInteger initialWorkCounter = new AtomicInteger(0);
        Observable<String> observable = Observable.just("nothing important");

        observable = observable.doOnNext(event -> {
            initialWorkCounter.incrementAndGet();
            System.out.println("Saving event: " + event + " to database");
        });

        observable.subscribe(event -> {
            //Performs casual work, increases counter needlessly
            System.out.println("Subscriber received event: " + event);
        });

        observable.subscribe(event -> {
            //Performs casual work, call observable create needlessly
            System.out.println("Subscriber received event: " + event);
        });

        assertEquals(2, initialWorkCounter.get());
    }

    /**
     * publish().connect() duet creates an artificial Subscriber immediately while keeping
     * just one upstream Subscriber.
     */
    @Test
    void publishConnectShouldEmitValuesToItsSubscriber() {
        AtomicInteger initialWorkCounter = new AtomicInteger(0);
        Observable<String> observable = Observable.create(subscriber -> {
            System.out.println("Doing some initial work");
            initialWorkCounter.incrementAndGet();
            subscriber.onNext("publishConnect");
            subscriber.onCompleted();
        });

        ConnectableObservable<String> published = observable.publish();
        published.connect();

        // Further subscriptions should not call create
        published.subscribe();
        published.subscribe();

        assertEquals(1, initialWorkCounter.get());
    }
}
