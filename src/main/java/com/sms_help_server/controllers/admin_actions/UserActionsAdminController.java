package com.sms_help_server.controllers.admin_actions;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users/")
public class UserActionsAdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/info")
    public ResponseEntity<SmsHelpUser> getFullInfoAboutUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email) {
        return ResponseEntity.ok(userService.findByIdOrEmail(userId, email));
    }

    @PatchMapping("/changeStatus")
    public ResponseEntity<SmsHelpUser> changeUserStatusByUserId(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestParam EntityStatus newStatus) {
        SmsHelpUser user = userService.findByIdOrEmail(userId, email);
        return ResponseEntity.ok(userService.updateUserStatus(user, newStatus));
    }
}
