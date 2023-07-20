package com.sms_help_server.services.email_service;

import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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
            throw new EmailException(e.getMessage());
        }
    }

    @Async
    @Override
    public void sendVerificationMessage(SmsHelpUser registeredUser, String link) {
        this.sendMessage(
                registeredUser.getEmail(),
                "Sms.help account verification",
                "Dear " + registeredUser.getNickname() + "!" +
                        "\nYou have been successfully registered on sms.help service!" +
                        "\nTo verify your account click on the link below:" +
                        "\n" + link
        );
    }

    @Async
    @Override
    public void sendPasswordResetLinkMessage(SmsHelpUser user, String resetPasswordLink) {
        this.sendMessage(
                user.getEmail(),
                "Password reset",
                "Dear " + user.getNickname() + ", your sms.help password reset link: " + resetPasswordLink
        );
    }

    @Async
    @Override
    public void sendSuccessfullPasswordResetMessage(SmsHelpUser user) {
        this.sendMessage(
                user.getEmail(),
                "Password reset",
                "Dear " + user.getNickname() + ", your sms.help password has been successfully changed."
        );
    }
}
