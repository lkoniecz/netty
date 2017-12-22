package rxjava.extensions;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

import java.util.concurrent.TimeUnit;

public class ReactiveDelay {

    public static void main(String[] args) {
        Observable<Integer> delajed = delayed(10);
        Subscription sub = delajed.subscribe(System.out::println);
        sub.unsubscribe();


    }

    static <T> Observable<T> delayed(T x) {
        return Observable.create(
                subscriber -> {
                    Runnable r = () -> {
                        sleep(5, TimeUnit.SECONDS);
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onNext(x);
                            subscriber.onCompleted();
                        }
                    };
                    final Thread thread = new Thread(r);
                    thread.start();
                    subscriber.add(Subscriptions.create(() -> {
                        System.out.println("interrupted");
                        thread.interrupt();
                    }));
                });
    }

    static void sleep(int timeout, TimeUnit unit) {
        try {
            unit.sleep(timeout);
        } catch (InterruptedException ignored) {
            //intentionally ignored
        }
    }
}
