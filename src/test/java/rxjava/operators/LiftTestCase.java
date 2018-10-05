package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.Subscriber;
import rxjava.utils.RxTestUtils;

public class LiftTestCase {

    @Test
    public void liftTestCase() {
        Observable
                .range(1, 9)
                .lift(toStringOfOdd())
                .subscribe(RxTestUtils::log);
    }

    <T> Observable.Operator<String, T> toStringOfOdd() {
        return new Observable.Operator<String, T>() {
            private boolean odd = true;

            @Override
            public Subscriber<? super T> call(Subscriber<? super String> child) {
                return new Subscriber<T>(child) {
                    @Override
                    public void onCompleted() {
                        child.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        child.onError(e);
                    }

                    @Override
                    public void onNext(T t) {
                        if (odd) {
                            child.onNext(t.toString());
                        } else {
                            request(1);
                        }
                        odd = !odd;
                    }
                };
            }
        };
    }
}
