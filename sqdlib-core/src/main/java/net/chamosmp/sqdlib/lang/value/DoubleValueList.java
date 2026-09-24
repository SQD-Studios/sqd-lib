package net.chamosmp.sqdlib.lang.value;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class DoubleValueList<T, E> implements List<DoubleValue<T, E>> {

    private final List<DoubleValue<T, E>> doubleValue = new ArrayList<>();

    @SafeVarargs
    DoubleValueList(DoubleValue<T, E>... doubleValues) {
        doubleValue.addAll(Arrays.asList(doubleValues));
    }

    public @Nullable Object get(Object object) {
        for (DoubleValue<T, E> doubleValue : doubleValue) {
            DoubleValue.ValueType i = doubleValue.contains(object);
            if (i != null) {
                if (i == DoubleValue.ValueType.FIRST) {
                    return doubleValue.secondValue;
                } else if (i == DoubleValue.ValueType.SECOND) {
                    return doubleValue.firstValue;
                }
            }
        }
        return null;
    }

    @Override
    public int size() {
        return doubleValue.size();
    }


    @Override
    public boolean isEmpty() {
        return doubleValue.isEmpty();
    }


    public boolean contains(Object o) {
        boolean contains = false;
        for (DoubleValue<T, E> doubleValue : doubleValue) {
            contains = doubleValue.contains(doubleValue) != null;
            if (contains) break;
        }
        return contains;
    }

    @Override
    public @NonNull Iterator<DoubleValue<T, E>> iterator() {
        return doubleValue.iterator();
    }

    @Override
    public @NonNull Object @NonNull [] toArray() {
        return doubleValue.toArray();
    }


    @Override
    public @NonNull <t> t @NonNull [] toArray(@NonNull t @NonNull [] a) {
        return doubleValue.toArray(a);
    }


    @Override
    public boolean add(DoubleValue<T, E> teDoubleValue) {
        return doubleValue.add(teDoubleValue);
    }

    @Override
    public boolean remove(Object o) {
        return doubleValue.remove(o);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> c) {
        return new HashSet<>(doubleValue).containsAll(c);
    }


    public boolean remove(DoubleValue<T, E> o) {
        return doubleValue.remove(o);
    }


    @Override
    public boolean addAll(@NonNull Collection<? extends DoubleValue<T, E>> c) {
        return doubleValue.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NonNull Collection<? extends DoubleValue<T, E>> c) {
        return doubleValue.addAll(index, c);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> c) {
        return doubleValue.retainAll(c);
    }


    public boolean removeAll(@NonNull Collection<?> c) {
        return doubleValue.removeAll(c);
    }


    @Override
    public void clear() {
        doubleValue.clear();
    }

    @Override
    public DoubleValue<T, E> get(int index) {
        return doubleValue.get(index);
    }

    @Override
    public DoubleValue<T, E> set(int index, DoubleValue<T, E> element) {
        return doubleValue.set(index, element);
    }

    @Override
    public void add(int index, DoubleValue<T, E> element) {
        doubleValue.add(index, element);
    }

    @Override
    public @NonNull DoubleValue<T, E> remove(int index) {
        return doubleValue.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return doubleValue.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return doubleValue.lastIndexOf(o);
    }

    @Override
    public @NonNull ListIterator<DoubleValue<T, E>> listIterator() {
        return doubleValue.listIterator();
    }

    @Override
    public @NonNull ListIterator<DoubleValue<T, E>> listIterator(int index) {
        return doubleValue.listIterator(index);
    }

    @Override
    public @NonNull List<DoubleValue<T, E>> subList(int fromIndex, int toIndex) {
        return doubleValue.subList(fromIndex, toIndex);
    }

    @SafeVarargs
    public static <First, Second> DoubleValueList<First, Second> of(DoubleValue<First, Second>... doubleValues) {
        return new DoubleValueList<>(doubleValues);
    }
}