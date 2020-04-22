package com.fmoraes.plangenerator.controllers.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        try {
            ZonedDateTime.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
