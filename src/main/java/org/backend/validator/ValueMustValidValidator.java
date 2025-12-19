package org.backend.validator;

import org.backend.validation.ValueMustValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class ValueMustValidValidator implements ConstraintValidator<ValueMustValid, String> {

    private List<String> valueAllowed;

    @Override
    public void initialize(ValueMustValid constraintAnnotation) {
        valueAllowed = Arrays.asList(constraintAnnotation.valueAllowed());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueAllowed.contains(value);
    }
}