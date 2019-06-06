package com.mobile.app.ws.ui.model.response;

import java.util.List;

//converts java object into outgoing JSON response
//only contains fields that will be returned and are safe to return

public class UserRest {
	
	private String userId; //not same as user Id in the database, randomly generated
	private String firstName;
	private String lastName;
	private String email;
	private List<AddressesRest> addresses;
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<AddressesRest> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressesRest> addresses) {
		this.addresses = addresses;
	}
}
