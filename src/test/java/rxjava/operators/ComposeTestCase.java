package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

public class ComposeTestCase {

    @Test
    public void ComposeTestCase() {
        Observable<Integer> numbers = Observable.just(1, 3, 4, 2, 10, 43, 32, 66);
        numbers
                .compose(higherThan(10))
                .subscribe(RxTestUtils::log);
    }

    private Observable.Transformer<Integer, Integer> higherThan(Integer value) {
        Observable.Transformer<Integer, Integer> transformer =
                integerObservable -> integerObservable.filter(integer -> integer > 10);

        return transformer;
    }
    
}
