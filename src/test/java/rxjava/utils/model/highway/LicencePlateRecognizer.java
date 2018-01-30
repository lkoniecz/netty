package rxjava.utils.model.highway;

import rx.Observable;

import java.util.concurrent.TimeUnit;

public class LicencePlateRecognizer {
    private static LicencePlateRecognizer INSTANCE = new LicencePlateRecognizer();

    private LicencePlateRecognizer() {

    }

    public static LicencePlateRecognizer getInstance() {
        return INSTANCE;
    }



    public Observable<LicensePlate> fastAlgo(CarPhoto photo) {
        LicensePlate plate = new LicensePlate(photo, "fast");
        return Observable
                .timer(photo.getQuality(), TimeUnit.SECONDS)
                .flatMap(i -> Observable.just(plate));
    }

    public Observable<LicensePlate> preciseAlgo(CarPhoto photo) {
        LicensePlate plate = new LicensePlate(photo, "precise");
        return Observable
                .timer(3 * photo.getQuality(), TimeUnit.SECONDS)
                .flatMap(i -> Observable.just(plate));

    }

    public Observable<LicensePlate> experimentalAlgo(CarPhoto photo) {
        LicensePlate plate = new LicensePlate(photo, "experimental");
        return Observable
                .timer(2 * photo.getQuality(), TimeUnit.SECONDS)
                .flatMap(i -> Observable.just(plate));
    }
}
