package io.jfluent.function;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;

import static java.util.Objects.requireNonNull;

/**
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Runner<T> {

    private final T object;
    private final Map<Boolean, Runnable> runs = new HashMap<>();

    public static <T> Runner<T> from(T object) {
        requireNonNull(object, "Object that conditions the execution of action should not be null.");
        return new Runner<>(object);
    }

    public <R> Runner<T> with(final Predicate<? super T> predicate, final Runnable run) {
        runs.putIfAbsent(predicate.test(object), run);
        return this;
    }

    public <R> Runner<T> otherwise(final Runnable defaultRun) {
        runs.putIfAbsent(true, defaultRun);
        return this;
    }

    public void run() {
        runs.getOrDefault(true, () -> {
            throw new IllegalStateException("");
        }).run();
    }
}
