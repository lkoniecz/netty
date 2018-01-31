package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.Person;
import rxjava.utils.RxTestUtils;

import java.util.ArrayList;
import java.util.List;

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
}
