package com.mobile.app.ws.service.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobile.app.ws.io.entity.UserEntity;
import com.mobile.app.ws.io.repositories.UserRepository;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.UserDto;

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
	
	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) throw new UsernameNotFoundException(email);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);	
		return returnValue; 
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEcnryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		
		UserDto returnValue = new UserDto();
		UserEntity userEntity = userRepository.findByUserId(userId);
		
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		
		BeanUtils.copyProperties(userEntity, returnValue);	
		
		return returnValue;
	}

}
