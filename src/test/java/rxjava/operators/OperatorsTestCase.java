package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func1;
import rxjava.utils.Person;
import rxjava.utils.Person.Gender;
import rxjava.utils.RxTestUtils;
import rxjava.utils.model.highway.CarPhoto;
import rxjava.utils.model.highway.LicencePlateRecognizer;
import rxjava.utils.model.highway.LicensePlate;
import rxjava.utils.model.shop.Customer;
import rxjava.utils.model.shop.Order;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static rx.Observable.timer;

public class OperatorsTestCase {
    @Test
    public void filterTesting() {
        List<Person> people = RxTestUtils.generatePoepleList();
        Observable<Person> peopleObs = Observable.from(people).filter(person -> person.getGender() == Gender.MALE);

        List<Person> receivedPeople = new ArrayList<>();
        peopleObs.subscribe(person -> receivedPeople.add(person));

        assertTrue(receivedPeople.stream().allMatch(person -> person.getGender() == Gender.MALE));
    }

    @Test
    public void mapTesting() {
        List<Person> people = RxTestUtils.generatePoepleList();
        Observable<Person> personObs = Observable.create(subscriber -> {
            subscriber.onNext(people.get(1));
            subscriber.onNext(people.get(2));
            subscriber.onCompleted();
        });

        Observable<Integer> nameObs = personObs
                .map(person -> person.getFirstName())
                .map(name -> name.length());
        List<Integer> receivedPeopleNamesLength = new ArrayList<>();

        nameObs.subscribe(name -> receivedPeopleNamesLength.add(name));
        assertTrue(receivedPeopleNamesLength.get(0) == 3); // Jan
        assertTrue(receivedPeopleNamesLength.get(1) == 6); // Stefan
    }

    /**
     *
     */
    @Test
    public void flatMapTesting() {
        Observable<String> obs = Observable.just("lukasz")
                .doOnNext(str -> System.out.println("w pierwszym doOnNext: " + str))
                .flatMap(str -> Observable.just("konieczny"))
                .doOnNext(str -> System.out.println("doOnNext: " + str));
        obs.subscribe();

        Observable<CarPhoto> cars =  Observable.just(new CarPhoto(10));

        // Func1<T, R> - R call(T t) -> takes T as argument and returns R
        // public final <R> Observable<R> map(Func1<? super T, ? extends R> func)
        Observable<Observable<LicensePlate>> plates = cars.map(new Func1<CarPhoto, Observable<LicensePlate>>() {
            @Override
            public Observable<LicensePlate> call(CarPhoto carPhoto) {
                return null;
            }
        });

        Observable<LicensePlate> plates2 = cars.flatMap(new Func1<CarPhoto, Observable<LicensePlate>>() {
            @Override
            public Observable<LicensePlate> call(CarPhoto carPhoto) {
                return null;
            }
        });
    }

    @Test
    public void oneToManyTransformationTesting() {
        Observable<Customer> customers = Observable.from(Arrays.asList(new Customer()));
        Observable<Order> orders = customers
                .flatMap(customer -> Observable.from(customer.getOrders()));

        //equal to
        orders = customers
                //.map(customer -> customer.getOrders())
                .map(new Func1<Customer, List<Order>>() {

                    @Override
                    public List<Order> call(Customer customer) {
                        return customer.getOrders();
                    }
                })
                .flatMap(new Func1<List<Order>, Observable<Order>>() {
                    @Override
                    public Observable<Order> call(List<Order> orders) {
                        return Observable.from(orders);
                    }
                });

        //equal to
        orders = customers.map(Customer::getOrders).flatMap(Observable::from);

        //equal to
        orders = customers.flatMapIterable(Customer::getOrders);
    }

    /**
     * <R> Observable<R> flatMap(
     *      Func1<T, Observable<R>> onNext,
     *      Func1<Throwable, Observable<R>> onError,
     *      Func0<Observable<R>> onCompleted)
     */
    @Test
    public void flatMapTakesFunctionsAsArguments() {

    }

    @Test
    public void delayMethodShouldDelayEmittingAnEventStartingFromTheBeginning() throws InterruptedException {
        List<String> words = Arrays.asList("Lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit");
        CountDownLatch latch = new CountDownLatch(words.size());

        List<String> orderedWords = new ArrayList<>();

        // This will emit sit on 3rd second, amet and elit on 4th and so on.
        Observable
                .from(words)
                .delay(word -> timer(word.length(), TimeUnit.SECONDS))
                .subscribe(word -> {
                    System.out.println(word);
                    latch.countDown();
                    orderedWords.add(word);
                });

        latch.await();
        assertEquals(3, orderedWords.get(0).length());
        assertEquals(4, orderedWords.get(1).length());
        assertEquals(4, orderedWords.get(2).length());
        assertEquals(5, orderedWords.get(3).length());
        assertEquals(5, orderedWords.get(4).length());
        assertEquals(5, orderedWords.get(5).length());
        assertEquals(8, orderedWords.get(6).length());
        assertEquals(10, orderedWords.get(7).length());
    }

    @Test
    public void contactMapShouldPreserveOrderFromUpstream() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(10);
        Observable<String> obs = Observable
                .just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .concatMap(dow -> {
                    switch(dow) {
                        case SUNDAY:
                            return Observable
                                    .interval(90, MILLISECONDS)
                                    .take(5)
                                    .map(i -> {
                                        latch.countDown();
                                        return "Sun-" + i;
                                    });
                        case MONDAY:
                            return Observable
                                    .interval(65, MILLISECONDS)
                                    .take(5)
                                    .map(i -> {
                                        latch.countDown();
                                        return "Mon-" + i;
                                    });
                        default:
                            return null;
                    }
                });

        obs.subscribe(onNext -> System.out.println("onNext: " + onNext),
                onError -> System.out.println("Error: " + onError),
                () -> System.out.println("Completed"));
        latch.await();
    }

    @Test
    public void flatMapShouldHavePossibilityToLimitTotalNumberOfConcurrentSubscriptions() {
        List<Integer> numbers = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        Observable<Integer> numbersObs = Observable.from(numbers).flatMap(number -> Observable.create(subscriber -> {
            System.out.println("Heavy computation for number: " + number);
            subscriber.onNext(number);
        }), 10);

        numbersObs.subscribe(System.out::println);
    }

    @Test
    public void someTest() {
        Observable
                .just(8, 9, 10)
                .doOnNext(i -> System.out.println("A: " + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: " + i))
                .map(i -> "#" + i * 10)
                .doOnNext(s -> System.out.println("C: " + s))
                .filter(s -> s.length() < 4)
                .subscribe(s -> System.out.println("D: " + s));
    }
}
