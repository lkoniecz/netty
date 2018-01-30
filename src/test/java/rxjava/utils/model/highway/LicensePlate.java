package rxjava.utils.model.highway;

import rx.Observable;

public class LicensePlate {
    private CarPhoto photo;
    private String algo;

    public LicensePlate(CarPhoto photo, String algo) {
        this.photo = photo;
        this.algo = algo;
    }

    public String getAlgo() {
        return algo;
    }
}
