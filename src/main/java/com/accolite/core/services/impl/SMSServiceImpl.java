package com.accolite.core.services.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accolite.config.SMSConfig;
import com.accolite.core.entity.User;
import com.accolite.core.services.SMSService;
import com.twilio.exception.TwilioException;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Service
public class SMSServiceImpl implements SMSService{

	private final static Logger logger = Logger.getLogger(SMSServiceImpl.class);
	
    private TwilioRestClient twilioRestClient;
    
    @Autowired
    public SMSServiceImpl(SMSConfig smsConfig) {
    	this.smsConfig = smsConfig;
    }
	
	public TwilioRestClient getTwilioRestClient() {
		return twilioRestClient;
	}

	@Autowired
	public void setTwilioRestClient() {
		this.twilioRestClient = new TwilioRestClient.Builder(this.getSmsConfig().getAccountSID(), this.getSmsConfig().getAuthToken()).build();
	}

	public SMSConfig getSmsConfig() {
		return smsConfig;
	}

	public void setSmsConfig(SMSConfig smsConfig) {
		this.smsConfig = smsConfig;
	}

	private SMSConfig smsConfig;
	
	@Override
	public String sendSMS(User user, String message) {
		MessageCreator messageCreator = new MessageCreator(
                new PhoneNumber(user.getNumber()),
                new PhoneNumber(smsConfig.getFromPhoneNumber()),
                message);
        try {
            messageCreator.create(this.twilioRestClient);
        } catch (TwilioException e) {
        	e.printStackTrace();
            throw new RuntimeException();
        }
		return "Sent Successfully";	
	}
	
}
