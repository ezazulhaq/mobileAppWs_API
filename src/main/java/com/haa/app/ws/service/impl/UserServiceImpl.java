package com.haa.app.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.haa.app.ws.exception.UserServiceException;
import com.haa.app.ws.io.entity.UserEntity;
import com.haa.app.ws.io.repositories.UserRepository;
import com.haa.app.ws.service.UserService;
import com.haa.app.ws.shared.Utils;
import com.haa.app.ws.shared.dto.AddressDto;
import com.haa.app.ws.shared.dto.UserDto;
import com.haa.app.ws.ui.model.response.ErrorMessages;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto createUser(UserDto user) {
		
		if(userRepository.findUserByEmail(user.getEmail()) != null) 
			throw new RuntimeException("Record Already Exist");
		
		for (int i = 0; i < user.getAddresses().size(); i++) {
			AddressDto addressDto = user.getAddresses().get(i);
			addressDto.setUserDetails(user);
			addressDto.setAddressId(utils.generateAddressId(utils.getAddressIdLength()));
			user.getAddresses().set(i, addressDto);
		}
		
		ModelMapper modelmapper = new ModelMapper();
		UserEntity userEntity = new UserEntity();
		//BeanUtils.copyProperties(user, userEntity);
		userEntity = modelmapper.map(user, UserEntity.class);
		
		String publicUserId = utils.generateUserId(utils.getUserIdLength());
		userEntity.setUserId(publicUserId);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storeUserDetails = userRepository.save(userEntity);
		
		UserDto returnValue = new UserDto();
		//BeanUtils.copyProperties(storeUserDetails, returnValue);
		returnValue = modelmapper.map(storeUserDetails, UserDto.class);
		
		return returnValue;
	}
	
	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findUserByEmail(email);

		if(userEntity == null)
			throw new UsernameNotFoundException(email);

		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
		
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findUserByEmail(email);
		
		if(userEntity == null)
			throw new UsernameNotFoundException(email);
			
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {

		UserDto returnValue = new UserDto();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity updateEntity = userRepository.save(userEntity);
		BeanUtils.copyProperties(updateEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String userId) {
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if(userEntity == null)
			throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userRepository.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto> returnValues = new ArrayList<UserDto>();
		
		if(page > 0)
			page = page - 1;
		
		PageRequest pagableRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage = userRepository.findAll(pagableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for (UserEntity userEntity : users) {
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(userEntity, userDto);
			returnValues.add(userDto);
		}
		
		return returnValues;
	}

}
