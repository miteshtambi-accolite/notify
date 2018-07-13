package com.accolite.rest.controllers;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accolite.core.entity.User;
import com.accolite.core.services.EmailService;

@RestController
@RequestMapping("")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @RequestMapping(method = RequestMethod.POST, value="/email")
    public String sendEmail(@RequestBody User user) throws FileNotFoundException {
        return emailService.sendEmail(user);
    }
    

    @RequestMapping(method = RequestMethod.POST, value="/emailWithAttachment")
    public String sendEmailWithAttachment(@RequestBody User user) throws FileNotFoundException {
        return emailService.sendEmailWithAttachment(user);
    }
}
