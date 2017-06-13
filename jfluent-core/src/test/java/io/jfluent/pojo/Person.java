package io.jfluent.pojo;

import lombok.Builder;
import lombok.Getter;

/**
 * This class represents a person.
 *
 * @author Mazlum Tosun
 */
@Builder
@Getter
public class Person {

    private String civility;
    private String firstName;
    private String lastName;
    private Integer age;
}
