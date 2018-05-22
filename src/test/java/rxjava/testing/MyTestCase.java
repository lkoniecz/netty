package rxjava.testing;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class MyTestCase {

    @Test
    public void sleepTesting() {
        Observable just = Observable.just("chuju");
        just.subscribe(event -> {
            RxTestUtils.sleep(2, TimeUnit.SECONDS);
            System.out.println(event);
        });

        Observable obs = Observable.create(subscriber -> {
           Runnable r = () -> {
               RxTestUtils.sleep(3, TimeUnit.SECONDS);
               if (!subscriber.isUnsubscribed()) {
                   subscriber.onNext("siema");
                   subscriber.onCompleted();
               }
           };
           new Thread(r).start();
        });

        obs.subscribe(System.out::println);
        RxTestUtils.sleep(20, TimeUnit.SECONDS);
        
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }
}
