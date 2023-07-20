package com.sms_help_server.controllers.user_actions;

import com.sms_help_server.controllers.BaseResponseDTO;
import com.sms_help_server.controllers.user_actions.dto.ChangePasswordRequestDTO;
import com.sms_help_server.controllers.user_actions.dto.ExtendedUserDTO;
import com.sms_help_server.controllers.user_actions.dto.ShortUserDTO;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.security.auth_service.AuthService;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
                        user.getBalance(),
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
                        user.getNickname(),
                        user.getBalance()
                )
        );
    }

    @PatchMapping("/changePassword")
    public ResponseEntity<BaseResponseDTO> changeUserPassword(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestBody ChangePasswordRequestDTO newPasswordDTO) {
        SmsHelpUser user = userService.findByIdOrEmail(userId, email);
        authService.updateUserPassword(user, newPasswordDTO.getNewPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponseDTO(
                        HttpStatus.OK.value(),
                        "Your password has ben successfully updated"
                ));
    }

    @GetMapping("/requestNewVerificationLink")
    public ResponseEntity<BaseResponseDTO> requestNewVerificationLink(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email) {
        authService.generateNewVerificationToken(userService.findByIdOrEmail(userId, email));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BaseResponseDTO(
                        HttpStatus.OK.value(),
                        "Password reset link was sent to your email."
                ));
    }
}
