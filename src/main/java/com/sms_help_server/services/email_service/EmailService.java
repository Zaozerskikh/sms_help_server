package com.sms_help_server.services.email_service;

import com.sms_help_server.entities.user.SmsHelpUser;

public interface EmailService {
     void sendMessage(String targetEmail, String subject, String text);

     void sendSuccessfullRegistrationMessage(SmsHelpUser registeredUser);

     void sendPasswordResetLinkMessage(SmsHelpUser user, String resetPasswordLink);

     void sendSuccessfullPasswordResetMessage(SmsHelpUser user);
}
