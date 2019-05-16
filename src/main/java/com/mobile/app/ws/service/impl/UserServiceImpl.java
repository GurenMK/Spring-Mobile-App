package com.mobile.app.ws.service.impl;

import javax.management.RuntimeErrorException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobile.app.ws.UserRepository;
import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;

import antlr.Utils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	com.mobile.app.ws.shared.Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto user) {
		
		//check if record already exists
		if (userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record already exists!");
		
		UserEntity userEntity = new UserEntity();
		BeanUtils.copyProperties(user, userEntity);		
		
		String publicUserIdString = utils.generateUserId(30);
		userEntity.setUserId(publicUserIdString);
		userEntity.setEcnryptedPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		
		UserEntity storedUserDetails = userRepository.save(userEntity); 
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(storedUserDetails, returnValue);
		
		return returnValue;
	}

}
