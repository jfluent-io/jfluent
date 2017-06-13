package io.jfluent.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;

import static java.util.Objects.requireNonNull;

/**
 * This class allows to get a value or a {@link Supplier} conditionally from {@link
 * Predicate}.<br>It corresponds to pattern matching with predicates.
 *
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Guards<T> {

    private final T object;
    private final Map<Boolean, Supplier<Object>> cases = new HashMap<>();

    /**
     * Initializes guards class from the given generic object.
     *
     * @param object generic object
     */
    public static <T> Guards<T> of(T object) {
        requireNonNull(object, "Object to match should not be null.");
        return new Guards<>(object);
    }

    /**
     * Matches a {@link Predicate} with a {@link Supplier} and them to guards class.<br>If the
     * predicate returns {@code true}, the supplier will be returned by the guard class.
     *
     * @param predicate a predicate
     * @param supplier  a supplier that will returned if the predicate return true
     */
    public <R> Guards<T> when(final Predicate<? super T> predicate, final Supplier<? extends R> supplier) {
        cases.putIfAbsent(predicate.test(object), supplier::get);
        return this;
    }

    /**
     * Does the same action of {@link #when(Predicate, Supplier)} method but the predicate is
     * associated to a value.
     */
    public <R> Guards<T> when(final Predicate<? super T> predicate, final R supplierValue) {
        cases.putIfAbsent(predicate.test(object), () -> supplierValue);
        return this;
    }

    /**
     * Adds a default case.
     *
     * @param supplier a default supplier
     * @return the default value returned by the given supplier
     */
    public <R> Guards<T> otherwise(final Supplier<? extends R> supplier) {
        cases.putIfAbsent(true, supplier::get);
        return this;
    }

    /**
     * Does the same action of {@link #otherwise(Supplier)} but in this case a default value is get
     * instead of a supplier.
     */
    public <R> Guards<T> otherwise(final R supplierValue) {
        cases.putIfAbsent(true, () -> supplierValue);
        return this;
    }

    /**
     * Applies the guards cases and returns the value when the associated predicate returns {@code
     * true}.
     *
     * @return a generic result value
     */
    @SuppressWarnings("unchecked")
    public <R> R apply() {
        return (R) cases.getOrDefault(true, () -> {
            throw new IllegalStateException("Error no case match the predicates");
        }).get();
    }
}
