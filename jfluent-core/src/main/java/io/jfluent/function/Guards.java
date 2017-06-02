package io.jfluent.function;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;

/**
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Guards<T> {

  private final T object;
  private final Map<Boolean, Supplier<Object>> cases = new HashMap<>();

  public static <T> Guards<T> of(T object) {
    requireNonNull(object, "Object to match should be null.");
    return new Guards<>(object);
  }

  public <R> Guards<T> when(final Predicate<T> predicate, final Supplier<R> supplier) {
    cases.putIfAbsent(predicate.test(object), supplier::get);
    return this;
  }

  public <R> Guards<T> when(final Predicate<T> predicate, final R supplierValue) {
    cases.putIfAbsent(predicate.test(object), () -> supplierValue);
    return this;
  }

  public <R> Guards<T> otherwise(final Supplier<R> supplier) {
    cases.putIfAbsent(true, supplier::get);
    return this;
  }

  public <R> Guards<T> otherwise(final R supplierValue) {
    cases.putIfAbsent(true, () -> supplierValue);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <R> R apply() {
    return (R) cases.getOrDefault(true, () -> {
      throw new IllegalStateException("");
    }).get();
  }
}
