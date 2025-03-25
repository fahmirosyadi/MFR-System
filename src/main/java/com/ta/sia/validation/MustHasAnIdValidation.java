package com.ta.sia.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ta.sia.entity.AbstractEntity;

public class MustHasAnIdValidation implements ConstraintValidator<MustHasAnId, AbstractEntity> {
	
	@Override
	public boolean isValid(AbstractEntity ae, ConstraintValidatorContext arg1) {
		return ae.getId() != null;
	}
}
