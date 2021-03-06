package com.haa.app.ws.ui.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haa.app.ws.service.UserService;
import com.haa.app.ws.shared.dto.UserDto;
import com.haa.app.ws.ui.model.request.UserDetailsRequestModel;
import com.haa.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("users")	//http://localhost:8080/users
public class UserController {
	
	@Autowired
	UserService userService;
	
	@GetMapping
	public String getUser()
	{
		return "get user was called";
	}
	
	/* @Request
	 * Add model class - UserDetailsRequestModel POJO class
	 * Fields for POST request to be added in POJO class
	 * firstName, lastName, email and password
	 * 
	 * @Response
	 * POST response to return in UserRest model class
	 */
	@PostMapping
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails)
	{
		UserRest returnValue = new UserRest();
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		
		UserDto createUser = userService.createUser(userDto);
		BeanUtils.copyProperties(createUser, returnValue);
		
		return returnValue;
	}
	
	@PutMapping
	public String updateUser()
	{
		return "update user was called";
	}
	
	@DeleteMapping
	public String deleteUser()
	{
		return "delete user was called";
	}
	
}
