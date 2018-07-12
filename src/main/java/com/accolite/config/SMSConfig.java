package com.accolite.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;;	

@Configuration
public class SMSConfig {

	@Value("${account.sid}")  // this is to read variable from application.properties
    private String accountSID;

    @Value("${auth.token}")
    private String authToken;
    
    @Value("${fromPhoneNumber}")
    private String fromPhoneNumber;
    
    public String getFromPhoneNumber() {
		return fromPhoneNumber;
	}

	public void setFromPhoneNumber(String fromPhoneNumber) {
		this.fromPhoneNumber = fromPhoneNumber;
	}

	public String getAccountSID() {
		return accountSID;
	}

	public void setAccountSID(String accountSID) {
		this.accountSID = accountSID;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
}
    
