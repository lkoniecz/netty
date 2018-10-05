package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Func1;
import rxjava.utils.Person;
import rxjava.utils.RxTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapTestCase {

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

    private Person createPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }

    @Test
    public void howMapWorks() {
        Observable
                .fromCallable(() -> createPerson("lukasz", "konieczny"))
                .map(person -> person.getFirstName())
                .delay(2, TimeUnit.SECONDS)
                .toBlocking()
                .subscribe(System.out::println);


    }
}
