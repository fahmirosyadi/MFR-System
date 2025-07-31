package com.mfr.system.entity;

import java.lang.reflect.Field;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.mfr.system.config.View;
import com.mfr.system.config.ValidGroup.Update;
import com.mfr.system.config.View.All;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@MappedSuperclass
public abstract class AbstractEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull(groups = Update.class, message = "Id Must Not be Null")
	@JsonView(All.class)
	protected Long id;
	
	@JsonView(View.All.class)
	public abstract String getLabel();
	
	@JsonView(View.All.class)
	public Long getValue() {
		// TODO Auto-generated method stub
		return this.getId();
	}
	
	@Transient
	@JsonIgnore
	public String getFindLikeQuery() {
		String result = "select * from " + this.getClass().getName() + " where ";
		int count = 0;
		for(Field f : this.getClass().getFields()) {
			if(count > 0) {
				result += " and ";
			}
			result += f.getName() + " like %:s%";
			count++;
		}
		return result;
	}
	
}
