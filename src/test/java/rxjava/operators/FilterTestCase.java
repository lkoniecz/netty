package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rxjava.utils.Person;
import rxjava.utils.Person.Gender;
import rxjava.utils.RxTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilterTestCase {
    @Test
    public void filterTesting() {
        List<Person> people = RxTestUtils.generatePoepleList();
        Observable<Person> peopleObs = Observable
                .from(people)
                .filter(person -> person.getGender() == Gender.MALE);

        List<Person> receivedPeople = new ArrayList<>();
        peopleObs.subscribe(person -> receivedPeople.add(person));

        assertTrue(receivedPeople.stream().allMatch(person -> person.getGender() == Gender.MALE));
    }
}
