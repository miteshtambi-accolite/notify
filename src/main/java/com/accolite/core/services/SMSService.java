package com.accolite.core.services;

import org.springframework.stereotype.Service;

import com.accolite.core.entity.User;

@Service
public interface SMSService {
	
	public String sendSMS(User user, String message);
}
