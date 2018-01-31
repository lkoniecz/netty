package rxjava.utils;

public class ValueHolder<T, R> {
    private T firstValue;
    private R secondValue;

    private ValueHolder() {

    }

    public static ValueHolder newInstance() {
        return new ValueHolder();
    }

    public ValueHolder withDefaultValues(T firstDefault, R secondDefault) {
        firstValue = firstDefault;
        secondValue = secondDefault;
        return this;
    }

    public T getFirstValue() {
        return firstValue;
    }

    public void setFirstValue(T firstValue) {
        this.firstValue = firstValue;
    }

    public R getSecondValue() {
        return secondValue;
    }

    public void setSecondValue(R secondValue) {
        this.secondValue = secondValue;
    }

    @Override
    public String toString() {
        return "firstValue=" + firstValue + ", secondValue=" + secondValue;
    }
}
