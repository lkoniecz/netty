package functional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> names = Arrays.asList("Lukasz", "Zenek", "Krycha", "Zdzichu", "Wacek", "Janusz");

        final String name = names.stream().reduce("Jan", (name1, name2) ->
                name1.length() >= name2.length() ? name1 : name2);
        System.out.println(name);

        names.stream().filter(Main::isFemale).forEach(System.out::println);

        List<String> peopleWithNamesLongerThan5 = names.stream()
                .filter(n -> n.length() >= 5)
                //BiConsumer<R, ? super T> accumulator,
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println(peopleWithNamesLongerThan5);

        List<Person> people = new ArrayList<>();
        people.add(new Person("Lukasz", 33));
        people.add(new Person("Zenek", 54));
        people.add(new Person("Krycha", 67));
        people.add(new Person("Zdzichu", 99));
        people.add(new Person("Wacek", 12));
        people.add(new Person("Janusz", 7));

        List<Person> peopleOlderThan20 = people.stream()
                .filter(Person::isOlderThan20)
                .collect(Collectors.toList());
        System.out.println(peopleOlderThan20);

        //Higher order functions
        List<Integer> numbers = Arrays.asList(1, 2, 5, 7, 123, 32, 55, 10, 243, 5, 54354, 53, 2, 88);
        Function<Integer, Predicate<Integer>> isGreaterThan = pivot -> number -> number > pivot;

        numbers.stream().filter(isGreaterThan.apply(5)).forEach(System.out::println);
    }

    public static Predicate<Integer> isHigherThan(int number) {
        return i -> i > number;
    }

    public void consume(List<String> a, String b) {
        System.out.println(a + b);
    }

    public static void consumeOne(List<String> a) {
        System.out.println(a);
    }

    public static void doSomeShitWithBiConsumer(BiConsumer<List<String>, String> accumulator) {
        List<String> lista = new ArrayList<>();
        accumulator.accept(lista, "lukasz");
    }

    public static boolean isFemale(String name) {
        return name.endsWith("a");
    }
}
