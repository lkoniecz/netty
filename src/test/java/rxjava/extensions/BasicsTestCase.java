package rxjava.extensions;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.Callable;

public class BasicsTestCase {

    @Test
    public void observerBasics() {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                RxTestUtils.log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                RxTestUtils.log("onError", e);
            }

            @Override
            public void onNext(String s) {
                RxTestUtils.log("onNext " + s);
            }
        };

        Observable<String> observable = Observable.just("lukaszek");
        observable.subscribe(observer);
    }

    @Test
    public void subscriberBasics() {
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                RxTestUtils.log("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                RxTestUtils.log("onError", e);
            }

            @Override
            public void onNext(String s) {
                RxTestUtils.log("onNext " + s);
            }
        };

        String dupa = "dupa";

        Observable<String> observable = Observable.fromCallable(() -> {
            if ("dupa".equals(dupa)) {
                subscriber.unsubscribe();
                throw new Exception("duuuuuuuuuupa");
            }
            return "sieeeeeeeeema";
        });
        observable.subscribe(subscriber);
    }
}
