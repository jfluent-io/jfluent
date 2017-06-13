package io.jfluent.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;

import static java.util.Objects.requireNonNull;

/**
 * This class allows to implements pattern matching. Many combination of pattern/value can be put in
 * this class.<br>Then the value of the good pattern is returned.<br>The result could be a value or
 * a {@link Supplier}.
 *
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Matcher<T> {

    private final T object;
    private final Map<T, Supplier<Object>> cases = new HashMap<>();

    /**
     * Initialzes matcher class with a generic object.
     *
     * @param object generic object
     * @return this
     */
    public static <T> Matcher<T> of(T object) {
        requireNonNull(object, "Object to match should not be null.");
        return new Matcher<>(object);
    }

    /**
     * Matches a value with a {@link Supplier} and them to the matcher class.<br>If the
     * value matches the global value, the supplier will be returned by the matcher class.
     *
     * @param value    value to check
     * @param supplier a supplier that will returned if the predicate return true
     * @return this
     */
    public <R> Matcher<T> when(final T value, final Supplier<? extends R> supplier) {
        cases.putIfAbsent(value, supplier::get);
        return this;
    }

    /**
     * Does the same action of {@link #when(Object, Supplier)} method but the value to check is
     * associated to a value instead of a supplier.
     */
    public <R> Matcher<T> when(final T value, final R supplierValue) {
        cases.putIfAbsent(value, () -> supplierValue);
        return this;
    }

    /**
     * Adds a default case.
     *
     * @param supplier a default supplier
     * @return the default value returned by the given supplier
     */
    public <R> Matcher<T> otherwise(final Supplier<? extends R> supplier) {
        cases.putIfAbsent(object, supplier::get);
        return this;
    }

    /**
     * Does the same action of {@link #otherwise(Supplier)} but in this case a default value is get
     * instead of a supplier.
     */
    public <R> Matcher<T> otherwise(final R supplierValue) {
        cases.putIfAbsent(object, () -> supplierValue);
        return this;
    }

    /**
     * Applies the cases and returns the value when the associated pattern matches the global value.
     *
     * @return a generic result value
     */
    @SuppressWarnings("unchecked")
    public <R> R apply() {
        return (R) cases.getOrDefault(object, () -> {
            throw new IllegalStateException("");
        }).get();
    }
}
