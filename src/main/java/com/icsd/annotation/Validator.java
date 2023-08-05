package com.icsd.annotation;

import com.icsd.exceptionhand.IcsdException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;

@Valid
public class Validator implements ConstraintValidator<ValidateCondition, FrequencyDTO> {

    @Override
    public boolean isValid(FrequencyDTO frequencyDTO, ConstraintValidatorContext constraintValidatorContext) {
        if (frequencyDTO.getFrequency() == Frequency.MONTHLY && frequencyDTO.getDay() == 0) {
            throw new IcsdException(constraintValidatorContext.getDefaultConstraintMessageTemplate());
        }
        if (frequencyDTO.getFrequency() == Frequency.MONTHLY && frequencyDTO.getDay() > 31) {
            throw new IcsdException("Day can only be between 1 to 31");
        }
        return true;
    }
}