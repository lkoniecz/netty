package rxjava.extensions;

import rx.Observable;
import rxjava.ReactiveTools;

import java.util.concurrent.TimeUnit;

public class ReactiveTimerAndInterval {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("create observable");
        Observable.timer(1, TimeUnit.SECONDS).subscribe((Long zero) -> ReactiveTools.log(zero));
        System.out.println("after create");

        Thread.sleep(2000);
    }
}
