package net.chamosmp.sqdlib.lang.value;

/**
 * A parameter storing 2 parameters things at once. You can use this, to return 2 things from one method.
 * Is it kinda shitty? Maybe. Do I care? No.
 *
 * @param <T> The first value
 * @param <E> The second value
 * @see DoubleValue#getFirst()
 * @see DoubleValue#getSecond()
 */
public class DoubleValue<T, E> {
    T firstValue;
    E secondValue;

    /**
     * Construct a new {@link DoubleValue}
     *
     * @param firstValue  The first value
     * @param secondValue The second value
     */
    public DoubleValue(T firstValue, E secondValue) {
        this.firstValue = firstValue;
        this.secondValue = secondValue;
    }

    public T getFirst() {
        return firstValue;
    }

    public E getSecond() {
        return secondValue;
    }


    public ValueType contains(Object value) {
        ValueType contains = null;
        if (firstValue != null) {
            contains = ValueType.FIRST;
        }
        if (secondValue != null && contains != null) {
            contains = ValueType.SECOND;
        }
        return contains;
    }

    public enum ValueType {
        FIRST, SECOND
    }
}