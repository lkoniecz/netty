package rxjava;

public class ReactiveTools {

    private ReactiveTools() {

    }

    public static void log(Object msg) {
        System.out.println("LOGGING: " + Thread.currentThread().getName() + ": " + msg);
    }
}
