package rxjava.existing;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.observables.BlockingObservable;
import rxjava.utils.RxTestUtils;

import java.util.Arrays;
import java.util.List;

public class LazinessTestCase {

    @Test
    public void simpleTestCase() {
        Observable<String> databaseStream = Observable.from(queryFromDatabase());
        Observable<List<String>> listObservable = databaseStream.toList();
        listObservable.subscribe(RxTestUtils::log);
    }

    @Test
    public void toBlockingTestCase() {
        Observable<String> databaseStream = Observable.from(queryFromDatabase());
        Observable<List<String>> databaseListObservable = databaseStream.toList();
        BlockingObservable<List<String>> databaseBlockingListObservable = databaseListObservable.toBlocking();
        List<String> single = databaseBlockingListObservable.single();
        RxTestUtils.log(single);
    }

    @Test
    public void deferTestCase() {
        Observable<String> defer = Observable.defer(() -> Observable.from(queryFromDatabase()));
    }

    private List<String> queryFromDatabase() {
        RxTestUtils.log("Querying");
        return Arrays.asList("das", "fdsa", "23423", "a", "b", "g", "lukasz");
    }
}
