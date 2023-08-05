package com.icsd.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Validator.class)
public @interface ValidateCondition {
     String message() default "Invalid Detail";
     Class<?>[] groups() default {};
     Class<? extends Payload>[] payload() default {};
}
