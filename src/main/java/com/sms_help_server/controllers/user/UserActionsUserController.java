package com.sms_help_server.controllers.user;

import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/{userId}/")
public class UserActionsUserController {
    @GetMapping("/hello")
    public String test(@PathVariable Long userId) {
        return "hello from user" + userId;
    }

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<SmsHelpUser> adminTest() {
        return userService.findAll();
    }
}
