package io.jfluent.function;

import org.apache.commons.lang.BooleanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;

/**
 * Monad that allows to compose and chain operation in order to validate many field of the given
 * object {@code <T>}.
 *
 * @author Mazlum Tosun
 */
@AllArgsConstructor
public class Validator<T> {

    private final T object;
    private final List<IllegalArgumentException> errors;

    /**
     * Static factory method that allows to create new {@link Validator} instance, with given
     * object.
     *
     * @param object current object
     * @return this
     */
    public static <T> Validator<T> of(final T object) {
        return new Validator<T>(object, new ArrayList<>());
    }

    /**
     * Projects validator on other object.
     *
     * @param otherObject other object
     * @return this
     */
    public <U> Validator<U> thenOf(final U otherObject) {
        return new Validator<>(otherObject, errors);
    }

    /**
     * Allows to validate a projection that contains current field to validate. Projection is
     * chained with a {@link Predicate} that matches with function return field.
     *
     * @param projection current projection
     * @param filter     current predicate
     * @param message    current message to add in exception list, if current {@link Predicate}
     *                   returns false
     * @return this
     */
    public <R> Validator<T> validate(final Function<? super T, ? extends R> projection,
                                     final Predicate<? super R> filter, final String message) {

        // Checks if current field is invalid. In this case an error message is added in a list that
        // contains all errors.
        final Predicate<T> isNotNull = Objects::nonNull;
        final Predicate<T> filterOnField = projection.andThen(filter::test)::apply;
        final boolean isValidField = isNotNull.and(filterOnField).test(object);

        Optional.of(isValidField)
                .filter(BooleanUtils::isFalse)
                .ifPresent(i -> errors.add(new IllegalArgumentException(message)));

        return this;
    }

    /**
     * Gets error messages, if it exists validation errors.<br>
     * If there are no error, current object in validator is returned, otherwise an
     * {@link IllegalArgumentException} is thrown with all error messages.*
     */
    public void execute() {

        if (errors.isEmpty()) {
            return;
        }

        // An exception is thrown with all error message.
        final IllegalArgumentException exception = new IllegalArgumentException();
        errors.forEach(exception::addSuppressed);
        throw exception;
    }
}

