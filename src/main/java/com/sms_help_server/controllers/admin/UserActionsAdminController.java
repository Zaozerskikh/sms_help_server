package com.sms_help_server.controllers.admin;

import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/")
public class UserActionsAdminController {
    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public List<SmsHelpUser> adminTest() {
        return userService.findAll();
    }
}
