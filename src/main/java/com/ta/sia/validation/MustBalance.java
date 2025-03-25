package com.ta.sia.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = MustBalanceValidation.class)
@Target({ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MustBalance {
	
	public String message()default "Must balance";
	public Class<?>[] groups()default{};
	public Class<? extends Payload>[] payload()default{};

}
