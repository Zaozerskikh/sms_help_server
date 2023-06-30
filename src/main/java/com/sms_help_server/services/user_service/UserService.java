package com.sms_help_server.services.user_service;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.user.SmsHelpUser;

import java.util.List;

public interface UserService {
    List<SmsHelpUser> findAll();

    SmsHelpUser findByEmail(String email);

    SmsHelpUser findById(Long id);

    SmsHelpUser findByIdOrEmail(Long id, String email);

    SmsHelpUser updateUserStatus(SmsHelpUser user, EntityStatus newStatus);
}
