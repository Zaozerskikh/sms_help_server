package com.sms_help_server.security.auth_service;

import com.sms_help_server.entities.password_reset_token.PasswordResetToken;
import com.sms_help_server.entities.user.SmsHelpUser;

public interface AuthService {
    SmsHelpUser register(SmsHelpUser user);

    String authentificate(String email, String password);

    String generatePasswordResetToken(SmsHelpUser user, String linkContext);

    SmsHelpUser resetUserPassword(String passwordResetToken, String newPassword);

    SmsHelpUser updateUserPassword(SmsHelpUser user, String newPassword);

    PasswordResetToken findAndCheckPasswordResetToken(String passwordResetToken);
}
