package com.ta.sia.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.JurnalDetails;


public class OrderableValidation implements ConstraintValidator<Orderable, Jurnal> {
	
	@Override
	public boolean isValid(Jurnal jurnal, ConstraintValidatorContext arg1) {
		return true;
	}
}
