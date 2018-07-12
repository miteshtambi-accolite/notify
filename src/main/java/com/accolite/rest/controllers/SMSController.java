package com.accolite.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.core.entity.User;
import com.accolite.core.services.SMSService;

@RestController
@RequestMapping(value = "/sms")
public class SMSController {

     private SMSService smsService;
	
	 public SMSService getSmsService() {
		return smsService;
	}

	 @Autowired
	public void setSmsService(SMSService smsService) {
		this.smsService = smsService;
	}

	@RequestMapping(method = RequestMethod.POST)
	    public String sendEmail(@RequestBody User user) {
	        return smsService.sendSMS(user, "Hello From Mitesh");
	    }
}
