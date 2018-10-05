package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func1;
import rxjava.utils.Person;
import rxjava.utils.model.highway.CarPhoto;
import rxjava.utils.model.highway.LicensePlate;
import rxjava.utils.model.shop.Customer;
import rxjava.utils.model.shop.Order;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlatMapTestCase {

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

    @Test
    public void flatMapShouldHavePossibilityToLimitTotalNumberOfConcurrentSubscriptions() {
        List<Integer> numbers = IntStream.range(0, 100).boxed().collect(Collectors.toList());
        Observable<Integer> numbersObs = Observable.from(numbers).flatMap(number -> Observable.create(subscriber -> {
            System.out.println("Heavy computation for number: " + number);
            subscriber.onNext(number);
        }), 10);

        numbersObs.subscribe(System.out::println);
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

    private Person createPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }

    @Test
    public void tryingToUnderstandFlatMap() {
        Observable<Person> person = Observable.fromCallable(() -> createPerson("lukasz", "konieczny"));

        Observable<Person> observable = person.flatMap(new Func1<Person, Observable<Person>>() {
            @Override
            public Observable<Person> call(Person person) {
                Person p = new Person();
                p.setFirstName(person.getFirstName());
                p.setLastName(person.getLastName());
                return Observable.just(p);
            }
        });
    }
}
