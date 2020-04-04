package com.haa.app.ws.service;

import java.util.List;

import com.haa.app.ws.shared.dto.AddressDto;

public interface AddressService {
	
	List<AddressDto> getAddresses(String id);

}
