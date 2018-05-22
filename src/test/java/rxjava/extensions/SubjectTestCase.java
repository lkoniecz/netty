package rxjava.extensions;

import org.junit.jupiter.api.Test;
import rx.functions.Action1;
import rx.subjects.AsyncSubject;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rxjava.utils.RxTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * A Subject is a sort of bridge or proxy that acts both as an Subscriber and as an Observable.
 */
public class SubjectTestCase {

    @Test
    public void publishSubjectShouldBeEager() {
        AtomicInteger messagesReceived = new AtomicInteger(0);
        PublishSubject<String> subject = PublishSubject.create();

        subject.onNext("first message");

        subject.subscribe(item -> {
            RxTestUtils.log(item);
            messagesReceived.incrementAndGet();
        });

        subject.onNext("second message");

        assertEquals(1, messagesReceived.get());
    }

    @Test
    public void subjectShouldDropSubsequentOnErrorNotificationsAfterCallingOnError() {
        AtomicInteger errorsReceived = new AtomicInteger(0);
        PublishSubject<String> subject = PublishSubject.create();

        subject.subscribe(
                s -> RxTestUtils.log(s),
                throwable -> {
                    RxTestUtils.log("Got error", throwable);
                    errorsReceived.incrementAndGet();
                }
        );

        subject.onError(new Exception("first error"));
        subject.onError(new Exception("second error"));

        assertEquals(1, errorsReceived.get());
    }

    @Test
    public void asyncSubjectShouldSendLastValueEmittedBeforeOnComplete() {
        final AtomicInteger lastReceivedValue = new AtomicInteger(0);
        AsyncSubject<Integer> subject = AsyncSubject.create();
        subject.onNext(30);
        subject.onNext(31);
        subject.onCompleted();

        subject.subscribe(number -> {
            lastReceivedValue.set(number);
        });

        assertEquals(31, lastReceivedValue.get());
    }

    @Test
    public void behavioralSubjectShouldSendMostRecentlyEmittedValueBeforeSubscription() {
        List<Integer> messages = new ArrayList();
        BehaviorSubject<Integer> subject = BehaviorSubject.create();
        subject.onNext(30);

        subject.subscribe(number -> messages.add(number));

        subject.onNext(31);
        subject.onCompleted();

        assertEquals(2, messages.size());
        assertEquals(30, messages.get(0).intValue());
        assertEquals(31, messages.get(1).intValue());
    }

    @Test
    public void replaySubjectShouldCacheAllEventsPushed() {
        List<Integer> messages = new ArrayList();
        ReplaySubject<Integer> subject = ReplaySubject.create();
        subject.onNext(28);
        subject.onNext(29);
        subject.onNext(30);

        subject.subscribe(number -> messages.add(number));

        subject.onNext(31);
        subject.onCompleted();

        assertEquals(4, messages.size());
    }
}
