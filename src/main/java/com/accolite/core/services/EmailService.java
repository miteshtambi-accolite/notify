package com.accolite.core.services;

import com.accolite.core.entity.User;

public interface EmailService {
    String sendEmail(User user);
    String sendEmailWithAttachment(User user);
}
