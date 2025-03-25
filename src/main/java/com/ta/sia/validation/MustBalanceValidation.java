package com.ta.sia.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.ta.sia.entity.Jurnal;
import com.ta.sia.entity.JurnalDetails;


public class MustBalanceValidation implements ConstraintValidator<MustBalance, Jurnal> {
	
	@Override
	public boolean isValid(Jurnal jurnal, ConstraintValidatorContext arg1) {
		Double total = Double.valueOf(0);
		for(JurnalDetails jd : jurnal.getJurnalDetails()) {
			total += jd.getDebet() - jd.getKredit();
		}
		boolean status = total == Double.valueOf(0)?true : false;
		return status;
	}
}
