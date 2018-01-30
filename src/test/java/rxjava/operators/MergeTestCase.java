package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.model.highway.CarPhoto;
import rxjava.utils.model.highway.LicencePlateRecognizer;
import rxjava.utils.model.highway.LicensePlate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergeTestCase {
    @Test
    public void mergeShouldReceiveEventsFromAllMergedObservables() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(3);
        CarPhoto photo = new CarPhoto(1);
        LicencePlateRecognizer recognizer = LicencePlateRecognizer.getInstance();
        Observable<LicensePlate> all = Observable.merge(
                recognizer.preciseAlgo(photo),
                recognizer.fastAlgo(photo),
                recognizer.experimentalAlgo(photo)
        );

        all.subscribe(plate -> {
            System.out.println("Got plate from photo: " + plate.getAlgo());
            latch.countDown();
        });

        latch.await();
    }

    @Test
    void mergedObservablesShouldEmitAtMostOneError() {
        Observable<String> obs1 = Observable.create(subscriber -> {
            subscriber.onNext("onNext from obs1");
            subscriber.onError(new Exception("error from obs1"));
        });

        obs1 = obs1.delay(1, TimeUnit.SECONDS);

        Observable<String> obs2 = Observable.create(subscriber -> {
            subscriber.onNext("onNext from obs2");
            subscriber.onError(new Exception("error from obs2"));
        });

        AtomicInteger errorsReceived = new AtomicInteger(0);
        obs1.mergeWith(obs2)
                .subscribe(onNextEvent -> System.out.println(onNextEvent), onErrorEvent -> {
                    errorsReceived.incrementAndGet();
                    System.out.println(onErrorEvent);
                }, () -> System.out.println("completed"));
        assertEquals(1, errorsReceived.get());
    }
}
