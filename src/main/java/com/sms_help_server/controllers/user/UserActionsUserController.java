package com.sms_help_server.controllers.user;

import com.sms_help_server.controllers.user.dto.ExtendedUserDTO;
import com.sms_help_server.controllers.user.dto.ShortUserDTO;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/{userId}/")
public class UserActionsUserController {
    @Autowired
    private UserService userService;

    @GetMapping("/extendedInfo")
    public ResponseEntity<ExtendedUserDTO> getExtendedInfoAboutUserByUserId(@PathVariable Long userId) {
        SmsHelpUser user = userService.findById(userId);
        return ResponseEntity.ok(
                new ExtendedUserDTO(
                        user.getUserId(),
                        user.getCreatedDate(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getTopUps(),
                        user.getNumberPurchases()
                )
        );
    }

    @GetMapping("/shortInfo")
    public ResponseEntity<ShortUserDTO> getShortInfoAboutUserByUserId(@PathVariable Long userId) {
        SmsHelpUser user = userService.findById(userId);
        return ResponseEntity.ok(
                new ShortUserDTO(
                        user.getUserId(),
                        user.getEmail(),
                        user.getNickname()
                )
        );
    }

    @PatchMapping("/changePassword/{newPassword}")
    public ResponseEntity<SmsHelpUser> changeUserPasswordByUserId(
            @PathVariable Long userId, @PathVariable String newPassword) {
        return ResponseEntity.ok(userService.updateUserPasswordByUserId(userId, newPassword));
    }
}
