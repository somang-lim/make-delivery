package com.sm.makedelivery.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RiderDTO {

	@NotNull
	private final String id;

	@NotNull
	private final String name;

	@NotNull
	private final String phone;

	@NotNull
	private final String address;

	@NotNull
	private final String updatedAt;

	@NotNull
	private final String fcmToken;


	@JsonCreator
	public RiderDTO(@JsonProperty(value = "id") String id,
					@JsonProperty(value = "name") String name,
					@JsonProperty(value = "phone") String phone,
					@JsonProperty(value = "address") String address,
					@JsonProperty(value = "updatedAt") String updatedAt,
					@JsonProperty(value = "fcmToken") String fcmToken
	) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.updatedAt = updatedAt;
		this.fcmToken = fcmToken;
	}

}
