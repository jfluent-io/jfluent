package io.jfluent.function;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;

/**
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Matcher<T> {

  private final T object;
  private final Map<T, Supplier<Object>> cases = new HashMap<>();

  public static <T> Matcher<T> of(T object) {
    requireNonNull(object, "Object to match should be null.");
    return new Matcher<>(object);
  }

  public <R> Matcher<T> when(final T value, final Supplier<R> supplier) {
    cases.putIfAbsent(value, supplier::get);
    return this;
  }

  public <R> Matcher<T> when(final T value, final R supplierValue) {
    cases.putIfAbsent(value, () -> supplierValue);
    return this;
  }

  public <R> Matcher<T> otherwise(final Supplier<R> supplier) {
    cases.putIfAbsent(object, supplier::get);
    return this;
  }

  public <R> Matcher<T> otherwise(final R supplierValue) {
    cases.putIfAbsent(object, () -> supplierValue);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <R> R apply() {
    return (R) cases.getOrDefault(object, () -> {
      throw new IllegalStateException("");
    }).get();
  }
}
