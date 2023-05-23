package com.sm.makedelivery.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class StoreCategoryDTO {

	private Long id;

	private String name;


	@JsonCreator
	public StoreCategoryDTO(@JsonProperty(value = "id") Long id, @JsonProperty(value = "name") String name) {
		this.id = id;
		this.name = name;
	}

}
