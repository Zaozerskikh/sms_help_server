package com.sms_help_server.services.email_service;

import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.security.exceptions.RegistrationException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Log
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String emailFrom;

    @Override
    public void sendMessage(String targetEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(targetEmail);
            message.setFrom(emailFrom);
            message.setSubject(subject);
            message.setText(text);
            sender.send(message);
            log.info("EMAIL SENT: " + message);
        } catch (Exception e) {
            throw new RegistrationException("email not valid");
        }
    }

    @Override
    public void sendSuccessfullRegistrationMessage(SmsHelpUser registeredUser) {
        this.sendMessage(
                registeredUser.getEmail(),
                "Registration completed",
                "Dear " + registeredUser.getNickname() + ", you have been successfully registered on sms.help service!"
        );
    }

    @Override
    public void sendPasswordResetLinkMessage(SmsHelpUser user, String resetPasswordLink) {
        this.sendMessage(
                user.getEmail(),
                "Password reset",
                "Dear " + user.getNickname() + ", your password reset link: " + resetPasswordLink
        );
    }

    @Override
    public void sendSuccessfullPasswordResetMessage(SmsHelpUser user) {
        this.sendMessage(
                user.getEmail(),
                "Password reset",
                "Dear " + user.getNickname() + ", your password has been successfully changed."
        );
    }
}
