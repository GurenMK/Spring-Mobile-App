package com.mobile.app.ws.ui.model.request;

public class UserLoginRequestModel {
	
	private String emailString;
	private String passwordString;
	
	
	public String getEmail() {
		return emailString;
	}
	
	public void setEmail(String emailString) {
		this.emailString = emailString;
	}
	
	public String getPassword() {
		return passwordString;
	}
	
	public void setPassword(String passwordString) {
		this.passwordString = passwordString;
	}
}
