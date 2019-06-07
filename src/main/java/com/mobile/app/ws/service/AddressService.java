package com.mobile.app.ws.service;

import java.util.List;

import com.mobile.app.ws.shared.dto.AddressDto;

public interface AddressService {
	List<AddressDto> getAddresses(String userId);
	AddressDto getAddress(String addressId);
}
