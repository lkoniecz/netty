package functional;

@FunctionalInterface
public interface LukaszInterface {

    void add(int a, int b);

    boolean equals(Object o);

    public static int doubleIt(int a) {
        return 2 * a;
    }
}
