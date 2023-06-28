package com.sms_help_server.controllers.admin;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users/{userId}")
public class UserActionsAdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<SmsHelpUser> getFullInfoAboutUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PatchMapping("/changeStatus/{newStatus}")
    public ResponseEntity<SmsHelpUser> changeUserStatusByUserId(
            @PathVariable Long userId, @PathVariable EntityStatus newStatus) {
        return ResponseEntity.ok(userService.updateUserStatusByUserId(userId, newStatus));
    }
}
