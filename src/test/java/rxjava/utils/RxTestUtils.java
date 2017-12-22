package rxjava.utils;

public class RxTestUtils {

    private RxTestUtils() {

    }

    public static void log(Object msg) {
        System.out.println("LOGGING: " + Thread.currentThread().getName() + ": " + msg);
    }

}
