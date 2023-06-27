package com.sms_help_server.services.user_service;

import com.sms_help_server.entities.user.SmsHelpUser;

import java.util.List;

public interface UserService {
    SmsHelpUser register(SmsHelpUser user);

    List<SmsHelpUser> findAll();

    SmsHelpUser findByEmail(String email);

    SmsHelpUser findById(Long id);
}
