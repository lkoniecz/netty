package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectTestCase {

    @Test
    public void CollectTestCase() {
        Observable<List<Integer>> all = Observable
                .range(10, 20)
                .collect(ArrayList::new, List::add);

        all.subscribe(event -> assertTrue(event.size() == 20));
    }
}
