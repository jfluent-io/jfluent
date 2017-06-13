package io.jfluent.function;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static java.util.Objects.requireNonNull;

/**
 * Revisits GOF factory design pattern with function composition
 *
 * @author Mazlum Tosun
 */
@RequiredArgsConstructor
public final class Factory<T> {

    @NonNull
    private final T value;
    private final Map<T, Supplier<Object>> suppliers = new HashMap<>();

    /**
     * Initializes composition class from the given value of type.
     *
     * @param value value
     * @return this
     */
    public static <T> Factory<T> from(final T value) {
        requireNonNull(value);
        return new Factory<>(value);
    }

    /**
     * Registers an entry in the factory class with the type and the given supplier.
     *
     * @param type     type
     * @param supplier supplier
     * @return this
     */
    public <R> Factory<T> register(final T type, final Supplier<? extends R> supplier) {
        suppliers.put(type, supplier::get);
        return this;
    }

    /**
     * Does the same operation of {@link #register(Object, Supplier)} method but the given type and
     * supplier are put in factory class, only if the given predicate returns true.
     *
     * @param type     type
     * @param supplier supplier
     * @return this
     */
    public <R> Factory<T> register(final Predicate<? super T> filter, final T type, final Supplier<R> supplier) {
        Optional.of(type).filter(filter).ifPresent(t -> register(t, supplier));
        return this;
    }

    /**
     * Gets the good instance of object by the given type.<br>
     * If the given type doesn't exist in suppliers, we throws an {@link IllegalStateException}.
     *
     * @param type type
     * @return the good instance object
     */
    public Object create(final T type) {

        return suppliers.getOrDefault(type, () -> {
            throw new IllegalStateException("Invalid type : " + type);
        }).get();
    }
}
