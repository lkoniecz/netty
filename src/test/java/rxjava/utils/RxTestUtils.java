package rxjava.utils;

import rxjava.utils.Person.Gender;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RxTestUtils {

    private RxTestUtils() {

    }

    public static void log(Object msg) {
        System.out.println("LOGGING: " + Thread.currentThread().getName() + ": " + msg);
    }

    public static List<Person> generatePoepleList() {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Łukasz", "Konieczny", LocalDate.of(1984, 04, 27), Gender.MALE));
        people.add(new Person("Jan", "Kowalski", LocalDate.of(1924, 04, 01), Gender.MALE));
        people.add(new Person("Stefan", "Pitol", LocalDate.of(2001, 01, 20), Gender.MALE));
        people.add(new Person("Jadzia", "Jadziowa", LocalDate.of(1954, 11, 15), Gender.FEMALE));
        return people;
    }
 }
