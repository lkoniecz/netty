package functional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class FunctionalDecorator {

    public static void main(String[] args) {
        Function<Integer, Integer> doubleId = (i) -> i * 2;
        Function<Integer, Integer> tripleIt = (i) -> i * 3;

        List<Function<Integer, Integer>> functions = Arrays.asList(doubleId, tripleIt);

        Optional<Function<Integer, Integer>> filter = functions.stream().reduce((current, next) -> current.compose(next));
        Integer number = filter.get().apply(20);
        System.out.println(number);
    }
}
