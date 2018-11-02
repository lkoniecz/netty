package rxjava.multithreading;

import rx.Observable;
import rxjava.utils.RxTestUtils;

import java.math.BigDecimal;

public class RxGroceries {

    private void log(String message) {
        RxTestUtils.log(message);
    }

    Observable<BigDecimal> purchase(String productName, int quantity) {
        return Observable.fromCallable(() ->
                doPurchase(productName, quantity));
    }

    BigDecimal doPurchase(String productName, int quantity) {
        log("Purchasing " + quantity + " " + productName);
        //real logic here
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BigDecimal priceForProduct = new BigDecimal(quantity * 50);
        log("Done " + quantity + " " + productName);
        return priceForProduct;
    }
}
