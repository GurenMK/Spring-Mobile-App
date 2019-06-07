package com.mobile.app.ws.ui.controller;

import java.util.List;
import java.lang.reflect.Type;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
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
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import com.mobile.app.ws.exceptions.UserServiceException;
import com.mobile.app.ws.service.AddressService;
import com.mobile.app.ws.service.UserService;
import com.mobile.app.ws.shared.dto.AddressDto;
import com.mobile.app.ws.shared.dto.UserDto;
import com.mobile.app.ws.ui.model.request.UserDetailsRequestModel;
import com.mobile.app.ws.ui.model.response.AddressesRest;
import com.mobile.app.ws.ui.model.response.ErrorMessages;
import com.mobile.app.ws.ui.model.response.OperationStatusModel;
import com.mobile.app.ws.ui.model.response.RequestOperationName;
import com.mobile.app.ws.ui.model.response.RequestOperationStatus;
import com.mobile.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users") //http://localhost:8080/mobile-app-ws/users
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressService addressesService;
	
	@GetMapping(path="/{userId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest getUser(@PathVariable String userId) {
		
		UserRest returnValue = new UserRest();
		UserDto userDto = userService.getUserByUserId(userId);
		
		ModelMapper modelMapper = new ModelMapper();
		returnValue = modelMapper.map(userDto, UserRest.class);
		
		return returnValue;
	}
	
	@PostMapping(
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
		// @RequestBody maps JSON pay-load to userDetailsRequestModel class
		
		UserRest returnValue = new UserRest();
		//if (userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		//UserDto userDto = new UserDto();
		//BeanUtils.copyProperties(userDetails, userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
	
		UserDto createdUser = userService.createUser(userDto);
		returnValue = modelMapper.map(createdUser, UserRest.class);
		
		return returnValue;
	}
	
	@PutMapping(path="/{userId}",
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserRest updateUser(@PathVariable String userId, @RequestBody UserDetailsRequestModel userDetails) {
		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(userId, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);
		
		return returnValue;
	}
	
	@DeleteMapping(path="/{userId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUser(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		userService.deleteUser(id);
		
		returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		return returnValue;
	}
	
	@GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserRest> getUsers(@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="25") int limit) {
		List<UserRest> returnValue = new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnValue.add(userModel);
		}
		return returnValue;
	}
	
	//http://localhost:8080/mobile-app-ws/users/<user_id>/addresses
	@GetMapping(path="/{userId}/addresses",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
	public Resources<AddressesRest> getUserAddresses(@PathVariable String userId) {
		
		List<AddressesRest> addressesListRestModel = new ArrayList<>();
		List<AddressDto> addressesDto = addressesService.getAddresses(userId);
		
		if (addressesDto != null && !addressesDto.isEmpty()) {
			Type listType = new TypeToken<List<AddressesRest>>() {}.getType();
			addressesListRestModel = new ModelMapper().map(addressesDto, listType);
			
			for (AddressesRest addressRest : addressesListRestModel) {
				Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressRest.getAddressId())).withSelfRel();
				addressRest.add(addressLink);
				
				Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
				addressRest.add(userLink);
			}
		}
		
		return new Resources<>(addressesListRestModel);
	}
	
	@GetMapping(path="/{userId}/addresses/{addressId}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, "application/hal+json"})
	public Resource<AddressesRest> getUserAddress(@PathVariable String userId, 
			@PathVariable String addressId) {

		AddressDto addressDto = addressService.getAddress(addressId);
		
		Link addressLink = linkTo(methodOn(UserController.class).getUserAddress(userId, addressId)).withSelfRel();
		Link userLink = linkTo(methodOn(UserController.class).getUser(userId)).withRel("user");
		Link addressesLink = linkTo(methodOn(UserController.class).getUserAddresses(userId)).withRel("addresses");
		//linkTo builds http://localhost:8080/mobile-app-ws/users, methoOn builds the rest
		
		AddressesRest addressesRestModel = new ModelMapper().map(addressDto, AddressesRest.class);
		addressesRestModel.add(addressLink);
		addressesRestModel.add(userLink);
		addressesRestModel.add(addressesLink);
		//add is supported because of extended ResourceSupport in AddressRest
		
		return new Resource<>(addressesRestModel);
	}
	
	//http://localhost:8080/mobile-app-ws/users/email-verification?token=<token_value>
	@GetMapping(path="/email-verification",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel verifyEmailToken(@RequestParam(value="token") String token){
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		returnValue.setOperationName(RequestOperationName.VERIFY_EMAIL.name());
		boolean isVerified = userService.verifyEmailToken(token);
		
		if (isVerified) 
			returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());
		else
			returnValue.setOperationResult(RequestOperationStatus.ERROR.name());
		
		return returnValue;
	}
	
}
