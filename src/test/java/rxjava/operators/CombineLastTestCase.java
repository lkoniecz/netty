package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.ValueHolder;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static rx.Observable.interval;

public class CombineLastTestCase {

    /**
     * Symmetric - faster stream, no matter which, is paired up with slower
     */
    @Test
    void combineLatestShouldEmitAllValuesSimultaneouslyTakingLatestValueFromSlowStream() throws InterruptedException {
        ValueHolder<Long, Long> holder = ValueHolder.newInstance();
        AtomicLong repeatedValues = new AtomicLong(0);
        Observable<Long> slow = Observable.interval(17, TimeUnit.MILLISECONDS);
        Observable<Long> fast = Observable.interval(10, TimeUnit.MILLISECONDS);
        Observable.combineLatest(
                slow, fast,
                (s, f) -> {
                    if (holder.getFirstValue() == s) {
                        repeatedValues.incrementAndGet();
                    }
                    holder.setFirstValue(s);
                    holder.setSecondValue(f);
                    return holder;
                }
        ).forEach(System.out::println);

        Thread.sleep(2000);
        assertTrue(holder.getFirstValue() < holder.getSecondValue());
        assertTrue(repeatedValues.get() > 0);
    }

    @Test
    void withLatestFromShouldPairSlowerStreamEventsWithLatestFromFaster() throws InterruptedException {
        ValueHolder<Long, Long> holder = ValueHolder.newInstance().withDefaultValues(0L, 0L);
        AtomicLong numberOfGaps = new AtomicLong(0); //some values from faster stream were missed

        Observable<String> fast = interval(10, TimeUnit.MILLISECONDS).map(x -> x.toString());
        Observable<String> slow = interval(17, TimeUnit.MILLISECONDS).map(x -> x.toString());
        slow.withLatestFrom(fast, (s, f) -> s + ":" + f).forEach(item -> {
            String[] itemParts = item.split(":");
            Long itemFromSlower = Long.valueOf(itemParts[0]);
            Long itemFromFaster = Long.valueOf(itemParts[1]);
            if (itemFromFaster - holder.getSecondValue() > 1L) {
                numberOfGaps.incrementAndGet();
            }
            holder.setFirstValue(itemFromSlower);
            holder.setSecondValue(itemFromFaster);
            System.out.println(holder);
        });

        Thread.sleep(2000);

        assertTrue(numberOfGaps.get() > 1L);
    }

    @Test
    public void withLatestFromDropsAllSlowEventsAppearingBeforeTheFirstFastEvent() throws InterruptedException {
        ValueHolder<Long, Long> holder = ValueHolder.newInstance().withDefaultValues(0L, 0L);
        final AtomicBoolean firstEmit = new AtomicBoolean(true);
        Observable<Long> slow = interval(10, TimeUnit.MILLISECONDS);
        Observable<Long> fast = interval(10, TimeUnit.MILLISECONDS).delay(1, TimeUnit.SECONDS);
        slow.withLatestFrom(fast, (s, f) -> s + ":" + f).forEach(event -> {
            if (firstEmit.get()) {
                String[] itemParts = event.split(":");
                Long itemFromSlower = Long.valueOf(itemParts[0]);
                Long itemFromFaster = Long.valueOf(itemParts[1]);
                holder.setFirstValue(itemFromSlower);
                holder.setSecondValue(itemFromFaster);
                firstEmit.set(false);
            }
            System.out.println(event);
        });

        Thread.sleep(2000);
        assertTrue(holder.getFirstValue() > holder.getSecondValue());
    }

    @Test
    public void combineLatestTest() {
        Observable<String> fast = interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x);
        Observable<String> slow = interval(17, TimeUnit.MILLISECONDS).map(x -> "S" + x);
        Observable
                .combineLatest(slow, fast, (s, f) -> f + " : " + s)
                .toBlocking()
                .forEach(System.out::println);
    }

    @Test
    public void withLatestFromTest() {
        Observable<String> fast = interval(10, TimeUnit.MILLISECONDS).map(x -> "F" + x);
        Observable<String> slow = interval(17, TimeUnit.MILLISECONDS).map(x -> "S" + x);
        slow
                .withLatestFrom(fast, (s, f) -> s + ":" + f)
                .toBlocking()
                .forEach(System.out::println);
    }
}
