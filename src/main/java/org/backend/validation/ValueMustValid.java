package org.backend.validation;

import org.backend.validator.ValueMustValidValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValueMustValidValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueMustValid {
    String message() default "NotValid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String[] valueAllowed();
}