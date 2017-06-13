package io.jfluent.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;

import static java.util.Objects.requireNonNull;

/**
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Action<T> {

    private final T object;
    private final Map<Boolean, Consumer<? super T>> actions = new HashMap<>();

    public static <T> Action<T> from(T object) {
        requireNonNull(object, "Object that conditions the execution of action should not be null.");
        return new Action<>(object);
    }

    public <R> Action<T> with(final Predicate<? super T> predicate, final Consumer<? super T> action) {
        actions.putIfAbsent(predicate.test(object), action);
        return this;
    }

    public <R> Action<T> otherwise(final Consumer<? super T> defaultAction) {
        actions.putIfAbsent(true, defaultAction);
        return this;
    }

    public void run() {
        actions.getOrDefault(true, t -> {
            throw new IllegalStateException("");
        }).accept(object);
    }
}
