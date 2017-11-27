package functional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Person {
    private static Random random = new Random();
    private String name;
    private int age;
    private List<String> items = new ArrayList<>();

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        items.add(name + "_" + random.nextInt(1000));
        items.add(name + "_"+ random.nextInt(1000));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getItems() {
        return items;
    }

    public boolean isOlderThan20() {
        return this.age > 20;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", items=" + items +
                '}';
    }
}