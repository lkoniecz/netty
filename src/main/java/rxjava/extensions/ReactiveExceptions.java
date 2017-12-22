package rxjava.extensions;

import rx.Observable;
import rx.Observer;
import rxjava.Tweet;

public class ReactiveExceptions {

    public static void main(String[] args) {
        Observer<Tweet> observer = new Observer<Tweet>() {
            final String failureStr = "fail";

            @Override
            public void onNext(Tweet tweet) {
                System.out.println("onNext");
                tweet.tweet();
                if (failureStr.equals(tweet.getText())) {
                    throw new RuntimeException("jeeebs");
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError " + e);
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }
        };

        Observable<Tweet> tweet = rxLoad("fail");
        tweet.subscribe(observer);

        System.out.println("--------------------------");

        tweet = Observable.just(new Tweet("hej just"));
        tweet.subscribe(observer);

        System.out.println("--------------------------");
        tweet.subscribe(tw -> tw.tweet());
    }

    private static Observable<Tweet> rxLoad(String msg) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(new Tweet(msg));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private static Observable<Tweet> rxLoadShort(int id) {
        return Observable.fromCallable(() -> new Tweet("siemka short " + id));
    }
}
