package net.chamosmp.sqdlib.lang.value;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class DoubleValueList<T, E> {

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
                    return doubleValue.firstValue;
                } else if (i == DoubleValue.ValueType.SECOND) {
                    return doubleValue.secondValue;
                }
            }
        }
        return null;
    }

    public int size() {
        return doubleValue.size();
    }


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

    public @NonNull Object[] toArray() {
        return doubleValue.toArray();
    }


    public @NonNull <t> t[] toArray(@NonNull t[] a) {
        return doubleValue.toArray(a);
    }


    public boolean add(DoubleValue<T, E> teDoubleValue) {
        return doubleValue.add(teDoubleValue);
    }


    public boolean remove(DoubleValue<T, E> o) {
        return doubleValue.remove(o);
    }


    public boolean addAll(@NonNull Collection<? extends DoubleValue<T, E>> c) {
        return doubleValue.addAll(c);
    }


    public boolean addAll(int index, @NonNull Collection<? extends DoubleValue<T, E>> c) {
        return doubleValue.addAll(index, c);
    }


    public boolean removeAll(@NonNull Collection<DoubleValue<T, E>> c) {
        return doubleValue.removeAll(c);
    }


    public void clear() {
        doubleValue.clear();
    }


    public DoubleValue<T, E> get(int index) {
        return doubleValue.get(index);
    }


    public DoubleValue<T, E> set(int index, DoubleValue<T, E> element) {
        return doubleValue.set(index, element);
    }


    public void add(int index, DoubleValue<T, E> element) {
        doubleValue.add(index, element);
    }


    public @NotNull DoubleValue<T, E> remove(int index) {
        return doubleValue.remove(index);
    }

    @SafeVarargs
    public static <First, Second> DoubleValueList<First, Second> of(DoubleValue<First, Second>... doubleValues) {
        return new DoubleValueList<>(doubleValues);
    }
}