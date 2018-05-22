package learning.netty.echo.server;

import java.time.LocalTime;

public class Tools {

    public static void log(String msg) {
        System.out.println(Thread.currentThread() + " " + LocalTime.now() + " " + msg);
    }
}
