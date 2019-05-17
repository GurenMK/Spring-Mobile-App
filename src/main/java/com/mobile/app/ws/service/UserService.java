package com.mobile.app.ws.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.mobile.app.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	UserDto createUser(UserDto user);
	UserDto getUser(String email);
	UserDetails loadUserByUsername(String email);
	UserDto getUserByUserId(String userId);
}
