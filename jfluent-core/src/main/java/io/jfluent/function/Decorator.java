package io.jfluent.function;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import lombok.AllArgsConstructor;

/**
 * Allows to compose some function and executes the function pipeline from the given generic
 * value.<br>
 * This composition allows to revisit GOF decorator with functions and a fluent way.
 *
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Decorator<T> {

  private final T value;
  private final Function<T, T> function;

  /**
   * Initializes composition class from the given value and an empty function.
   * 
   * @param value value
   * @return this
   */
  public static <T> Decorator<T> from(final T value) {
    requireNonNull(value);
    return new Decorator<>(value, Function.identity());
  }

  /**
   * Composes the given function with the global function of class.
   * 
   * @param otherFunction function to compose
   * @return this
   */
  public Decorator<T> with(final Function<T, T> otherFunction) {
    return new Decorator<>(this.value, function.andThen(otherFunction));
  }

  /**
   * Applies all decorators and executes the global function on the given value.
   * 
   * @return the final value
   */
  public T apply() {
    return this.function.apply(value);
  }
}
