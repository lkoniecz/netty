package rxjava;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReactiveLearning {

    public static void main(String[] args) {
        Observer<Tweet> observer = new Observer<Tweet>() {
            @Override
            public void onNext(Tweet tweet) {
                tweet.tweet();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }
        };

        Subscriber<Tweet> subscriber = new Subscriber<Tweet>() {
            @Override
            public void onNext(Tweet tweet) {
                if (tweet.getText().contains("Java")) {
                    unsubscribe();
                }
                tweet.tweet();
            }

            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };

        List<Tweet> tweetList = IntStream.range(1, 10).boxed().map(item -> new Tweet("siema " + item)).collect(Collectors.toList());
        Observable<Tweet> tweets = Observable.from(tweetList);
        Subscription observerSubscription = tweets.subscribe(observer);
        Subscription subscriberSubscription = tweets.subscribe(subscriber);

        Observable justOneTweet = Observable.just(new Tweet("just one tweet"));
        justOneTweet.subscribe(subscriber);

        Observable<Integer> ints = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                ReactiveTools.log("Create");
                subscriber.onNext(5);
                subscriber.onNext(6);
                subscriber.onNext(7);
                subscriber.onCompleted();
                ReactiveTools.log("Completed");
            }
        });

        ReactiveTools.log("Starting");
        ints.subscribe(i -> ReactiveTools.log("Element: " + i));
        ReactiveTools.log("Exit");
    }
}
