package com.haa.app.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haa.app.ws.exception.UserServiceException;
import com.haa.app.ws.service.AddressService;
import com.haa.app.ws.service.UserService;
import com.haa.app.ws.shared.dto.AddressDto;
import com.haa.app.ws.shared.dto.UserDto;
import com.haa.app.ws.ui.model.request.UserDetailsRequestModel;
import com.haa.app.ws.ui.model.response.AddressRest;
import com.haa.app.ws.ui.model.response.ErrorMessages;
import com.haa.app.ws.ui.model.response.OperationStatusModel;
import com.haa.app.ws.ui.model.response.RequestOperationStatus;
import com.haa.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")	//http://localhost:8080/users
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressService addressesService;
	
	@GetMapping(path = "/{id}", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public UserRest getUser(@PathVariable String id)
	{
		UserRest returnValue = new UserRest();
		
		UserDto userDto = userService.getUserByUserId(id);
		BeanUtils.copyProperties(userDto, returnValue);

		return returnValue;
	}
	
	/* @Request
	 * Add model class - UserDetailsRequestModel POJO class
	 * Fields for POST request to be added in POJO class
	 * firstName, lastName, email and password
	 * 
	 * @Response
	 * POST response to return in UserRest model class
	 */
	@PostMapping(
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception
	{
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		
		UserDto createUser = userService.createUser(userDto);
		//BeanUtils.copyProperties(createUser, returnValue);
		returnValue = modelMapper.map(createUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping( path = "/{id}",
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		
		if(userDetails.getFirstName().isEmpty() || userDetails.getLastName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto updateUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updateUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping( path = "/{id}",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public OperationStatusModel deleteUser(@PathVariable String id)
	{
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		
		return returnValue;
	}
	
	@GetMapping(			
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }
			)
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
									@RequestParam(value = "limit", defaultValue = "25") int limit)
	{
		List<UserRest> returnvalue = new ArrayList<UserRest>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnvalue.add(userModel);
		}
		
		return returnvalue;
	}
	
	// url - http://localhost:8080/mobile-app-ws/{id}/addresses
	@GetMapping(path = "/{id}/addresses", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public List<AddressRest> getUserAddress(@PathVariable String id)
	{
		List<AddressRest> returnValue = new ArrayList<AddressRest>();
		
		List<AddressDto> addressesDto = addressesService.getAddresses(id);
		
		if(addressesDto != null && !addressesDto.isEmpty())
		{
			Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}

		return returnValue;
	}
	
	// url - http://localhost:8080/mobile-app-ws/{id}/addresses/{addId}
	@GetMapping(path = "/{id}/addresses/{addressId}", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public AddressRest getUserAddressId(@PathVariable String addressId)
	{
		AddressRest returnValue = new AddressRest();
		ModelMapper modelMapper = new ModelMapper();
		
		AddressDto addressesDto = addressService.getAddressId(addressId);
		returnValue = modelMapper.map(addressesDto, AddressRest.class);

		return returnValue;
	}
	
}
