package rxjava.operators;

import org.junit.jupiter.api.Test;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rxjava.utils.RxTestUtils;

public class GroupByTestCase {

    class Person {
        private Integer id;
        private String name;

        public Person(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void groupByTestCase() {
        long a = 1;
        int b = 1;
        System.out.println(a == b);
        Person p1 = new Person(1, "lukasz");
        Person p2 = new Person(1, "zenek");
        Person p3 = new Person(2, "lukasz");
        Person p4 = new Person(4, "lukas2z");
        Person p5 = new Person(43, "lukasz4");
        Person p6 = new Person(7, "januch");
        Person p7 = new Person(15, "januch");


        Observable<Person> events = Observable.just(p1, p2, p3, p4, p5, p6, p7);

        Observable<GroupedObservable<Integer, Person>> grouped = events.groupBy(person -> person.getId());
        grouped.subscribe(new Action1<GroupedObservable<Integer, Person>>() {
            @Override
            public void call(GroupedObservable<Integer, Person> integerPersonGroupedObservable) {
                RxTestUtils.log(integerPersonGroupedObservable.getKey());
            }
        });

    }
}
