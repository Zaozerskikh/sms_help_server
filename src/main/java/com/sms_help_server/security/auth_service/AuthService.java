package com.sms_help_server.security.auth_service;

import com.sms_help_server.entities.tokens.password_reset_token.PasswordResetToken;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;

public interface AuthService {
    SmsHelpUser register(String nickname, String email, String password);

    String authentificate(String email, String password);

    String generatePasswordResetToken(SmsHelpUser user);

    SmsHelpUser resetUserPassword(String passwordResetToken, String newPassword);

    SmsHelpUser updateUserPassword(SmsHelpUser user, String newPassword);

    PasswordResetToken findAndCheckPasswordResetToken(String passwordResetToken);

    SmsHelpUser verifyUser(String token);

    String generateNewVerificationToken(SmsHelpUser user);
}
