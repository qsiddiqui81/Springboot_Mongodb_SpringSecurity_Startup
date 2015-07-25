package com.newDemo.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
	
	MALE("male"), FEMALE("female"), OTHERS("others");
	
	private String value;
	Gender(String value){
		this.value = value;
	}
	
	@JsonValue
    public String toValue() {
        return value;
    }
}