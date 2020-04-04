package com.haa.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haa.app.ws.io.entity.AddressEntity;
import com.haa.app.ws.io.entity.UserEntity;
import com.haa.app.ws.io.repositories.AddressRepository;
import com.haa.app.ws.io.repositories.UserRepository;
import com.haa.app.ws.service.AddressService;
import com.haa.app.ws.shared.dto.AddressDto;

@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	AddressRepository addressRepository;

	@Override
	public List<AddressDto> getAddresses(String id) {
		
		List<AddressDto> returnValues = new ArrayList<AddressDto>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(id);
		
		if(userEntity==null)
			return returnValues;
		
		Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
		for (AddressEntity addressEntity : addresses) {
			returnValues.add(modelMapper.map(addressEntity, AddressDto.class));
		}

		return returnValues;
	}

	@Override
	public AddressDto getAddressId(String addressId) {
		
		AddressDto returnValue = new AddressDto();
		ModelMapper modelMapper = new ModelMapper();
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if (addressEntity != null) {
			returnValue = modelMapper.map(addressEntity, AddressDto.class);
		}

		return returnValue;
	}

}
