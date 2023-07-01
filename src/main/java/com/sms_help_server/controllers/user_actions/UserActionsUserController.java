package com.sms_help_server.controllers.user_actions;

import com.sms_help_server.controllers.user_actions.dto.ChangePasswordRequestDTO;
import com.sms_help_server.controllers.user_actions.dto.ExtendedUserDTO;
import com.sms_help_server.controllers.user_actions.dto.ShortUserDTO;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.security.auth_service.AuthService;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/")
public class UserActionsUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @GetMapping("/extendedInfo")
    public ResponseEntity<ExtendedUserDTO> getExtendedInfoAboutUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email) {
        SmsHelpUser user = userService.findByIdOrEmail(userId, email);
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
    public ResponseEntity<ShortUserDTO> getShortInfoAboutUser(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email) {
        SmsHelpUser user = userService.findByIdOrEmail(userId, email);
        return ResponseEntity.ok(
                new ShortUserDTO(
                        user.getUserId(),
                        user.getEmail(),
                        user.getNickname()
                )
        );
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<SmsHelpUser> changeUserPassword(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestBody ChangePasswordRequestDTO newPasswordDTO) {
        SmsHelpUser user = userService.findByIdOrEmail(userId, email);
        return ResponseEntity.ok(authService.updateUserPassword(user, newPasswordDTO.getNewPassword()));
    }

    @GetMapping("/requestNewVerificationLink")
    public ResponseEntity<String> requestNewVerificationLink(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email) {
        authService.generateNewVerificationToken(userService.findByIdOrEmail(userId, email));
        return ResponseEntity.ok("generated");
    }
}
