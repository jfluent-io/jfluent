package io.jfluent.function;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.Test;

import io.jfluent.pojo.Person;
import lombok.val;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Contains all tests that concern {@link Guards} class.
 *
 * @author Mazlum Tosun
 */
public class GuardsTest {

    @Test
    public void givenAnObject_whenExecuteGuardsWithSuppliers_thenExpectedResult() {

        // Given.
        val person = Person.builder().civility("MR").firstName("Mazlum").lastName("Tosun").age(45).build();
        val expectedResult = "Mazlum";

        // When.
        final String result = Guards.of(person)
                .when(p -> p.getFirstName().startsWith("Mazlum"), () -> "Mazlum")
                .when(p -> p.getFirstName().equals("Roronoa"), () -> "Roronoa")
                .when(p -> p.getFirstName().startsWith("Toto"), () -> "Mickael")
                .otherwise(() -> "Default name")
                .apply();

        // Then.
        assertThat(result).isNotNull().isNotEmpty().isEqualTo(expectedResult);
    }

    @Test
    public void givenAnObject_whenExecuteGuardsWithValues_thenExpectedResult() {

        // Given.
        val person = Person.builder().civility("MR").firstName("Roronoa").lastName("Zorro").age(35).build();
        val expectedResult = "Roronoa";

        // When.
        final String result = Guards.of(person)
                .when(p -> p.getFirstName().startsWith("Mazlum"), "Mazlum")
                .when(p -> p.getFirstName().equals("Roronoa"), "Roronoa")
                .when(p -> p.getFirstName().startsWith("Toto"), "Mickael")
                .otherwise(() -> "Default name")
                .apply();

        // Then.
        assertThat(result).isNotNull().isNotEmpty().isEqualTo(expectedResult);
    }

    @Test
    public void givenAnObject_whenExecuteGuardsWithDefaultCases_thenExpectedResults() {

        // Given.
        val person = Person.builder().civility("MR").firstName("Zizou").lastName("Zidane").age(35).build();
        val expectedResult = "Default name";

        // When.
        final String result1 = Guards.of(person)
                .when(p -> p.getFirstName().startsWith("Mazlum"), () -> "Mazlum")
                .when(p -> p.getFirstName().equals("Roronoa"), () -> "Roronoa")
                .when(p -> p.getFirstName().startsWith("Toto"), () -> "Mickael")
                .otherwise(() -> "Default name")
                .apply();

        final String result2 = Guards.of(person)
                .when(p -> p.getFirstName().startsWith("Mazlum"), "Mazlum")
                .when(p -> p.getFirstName().equals("Roronoa"), "Roronoa")
                .when(p -> p.getFirstName().startsWith("Toto"), "Mickael")
                .otherwise("Default name")
                .apply();

        // Then.
        assertThat(result1).isNotNull().isNotEmpty().isEqualTo(expectedResult);
        assertThat(result2).isNotNull().isNotEmpty().isEqualTo(expectedResult);
    }

    @Test
    public void givenAnObject_whenBadPredicatesAndNoDefaultCase_thenIllegalStateExceptionIsThrown() {

        // Given.
        val person = Person.builder().civility("MR").firstName("Zizou").lastName("Zidane").age(35).build();

        // When.
        final ThrowingCallable action = () -> Guards.of(person)
                .when(p -> p.getFirstName().startsWith("Mazlum"), () -> "Mazlum")
                .when(p -> p.getFirstName().equals("Roronoa"), () -> "Roronoa")
                .when(p -> p.getFirstName().startsWith("Toto"), () -> "Mickael")
                .apply();

        // Then.
        assertThatThrownBy(action)
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("Error no case match the predicates");
    }
}
