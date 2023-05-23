package com.sm.makedelivery.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class CartItemDTO {

	private final String name;

	private final Long price;

	private final Long storeId;

	private final List<CartOptionDTO> optionList;


	@JsonCreator
	public CartItemDTO(@JsonProperty(value = "name") String name,
					   @JsonProperty(value = "price") Long price,
					   @JsonProperty(value = "storeId") Long storeId,
					   @JsonProperty(value = "optionList") List<CartOptionDTO> optionList
	) {
		this.name = name;
		this.price = price;
		this.storeId = storeId;
		this.optionList = optionList;
	}

}
