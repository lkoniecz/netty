package functional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Lukasz", "Zenek", "Krycha", "Zdzichu", "Wacek", "Janusz");

        final String name = names.stream().reduce("Jan", (name1, name2) ->
                name1.length() >= name2.length() ? name1 : name2);
        System.out.println(name);

        names.stream().filter(n -> Main.isFemale(n)).forEach(System.out::println);

        Optional<String> firstName = names.stream().min((name1, name2) -> name1.compareTo(name2));
        System.out.println(firstName.get());
    }

    public static boolean isFemale(String name) {
        return name.endsWith("a");
    }
}
