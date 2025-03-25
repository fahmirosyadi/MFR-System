package com.ta.sia.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.ta.sia.validation.MustBalanceValidation;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Orderable {
	
	public Class<?>[] groups()default{};
	public Class<? extends Payload>[] payload()default{};

}
