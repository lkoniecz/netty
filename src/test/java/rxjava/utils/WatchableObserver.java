package rxjava.utils;

import rx.Observer;

public class WatchableObserver<T> implements Observer<T> {
    private boolean onCompletedCalled = false;
    private long onNextCounter = 0;

    @Override
    public void onCompleted() {
        onCompletedCalled = true;
        System.out.println("onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        System.out.println("onError " + e.getMessage());
    }

    @Override
    public void onNext(T t) {
        onNextCounter++;
        System.out.println("onNext: " + onNextCounter + " " + t);
    }

    public boolean isOnCompletedCalled() {
        return onCompletedCalled;
    }

    public long getOnNextCounter() {
        return onNextCounter;
    }
}
