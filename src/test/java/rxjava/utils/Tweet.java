package rxjava.utils;

public class Tweet {
    private String message;

    public Tweet(String message) {
        this.message = message;
    }

    public void tweet() {
        System.out.println("Tweet: " + message);
    }

    public String getText() {
        return message;
    }

    @Override
    public String toString() {
        return "Tweet{ message='" + message + '\'' +'}';
    }
}
