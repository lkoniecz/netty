package rxjava.extensions;

import rx.Observable;
import rx.Subscription;
import java.math.BigInteger;

public class ReactiveCaching {

    private static final BigInteger ZERO = new BigInteger("0");
    private static final BigInteger ONE = new BigInteger("1");

    public static void main(String[] args) throws InterruptedException {
        Observable<Integer> obs = Observable.<Integer>create(sub -> {
            System.out.println("create");
            sub.onNext(69);
        }).cache();

        obs.subscribe(sub -> System.out.println(sub));
        obs.subscribe(sub -> System.out.println(sub));

        Observable<BigInteger> naturalNumbers = Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        BigInteger i = ZERO;
                        while (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(i);
                            System.out.println(i);
                            i = i.add(ONE);
                        }
                    };
                    new Thread(r).start();
                });

        Subscription subscription = naturalNumbers.subscribe();
        //after some time...
        Thread.sleep(1000);
        subscription.unsubscribe();
    }
}
