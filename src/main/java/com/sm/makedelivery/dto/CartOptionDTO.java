package com.sm.makedelivery.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CartOptionDTO {

	private final Long optionId;

	private final String name;

	private final Long price;


	@JsonCreator
	public CartOptionDTO(@JsonProperty(value = "optionId") Long optionId,
						 @JsonProperty(value = "name") String name,
					     @JsonProperty(value = "price") Long price) {
		this.optionId = optionId;
		this.name = name;
		this.price = price;
	}

}
