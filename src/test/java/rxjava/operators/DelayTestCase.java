package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class DelayTestCase {

    @Test
    public void delayTest() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        Observable
                .from(numbers)
                .delay(2, TimeUnit.SECONDS)
                .toBlocking()
                .subscribe(System.out::println);

    }
}
