package com.sms_help_server.controllers.admin_actions;

import com.sms_help_server.controllers.admin_actions.dto.PhoneNumberRegistrationRequestDTO;
import com.sms_help_server.controllers.admin_actions.dto.PhoneNumberShortInfoDTO;
import com.sms_help_server.entities.phone_number.PhoneNumber;
import com.sms_help_server.services.phone_number_service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/numbers")
public class PhoneNumberActionsAdminController {
    @Autowired
    private PhoneNumberService phoneNumberService;

    @PutMapping("/add")
    public ResponseEntity<PhoneNumber> registerNewPhoneNumber(
            @RequestBody PhoneNumberRegistrationRequestDTO phoneNumberRegistrationRequestDTO) {
        return ResponseEntity.ok(phoneNumberService.registerNewPhoneNumber(phoneNumberRegistrationRequestDTO));
    }

    @GetMapping(value = "/shortInfo")
    public ResponseEntity<PhoneNumberShortInfoDTO> getShortInfoAboutPhoneNumber(
            @RequestParam(required = false) Long numberId,
            @RequestParam(required = false) String number) {
        PhoneNumber phoneNumber = phoneNumberService.findPhoneNumberByIdOrNumber(numberId, number);
        return ResponseEntity.ok(
                new PhoneNumberShortInfoDTO(
                        phoneNumber.getNumberId(),
                        phoneNumber.getNumber(),
                        phoneNumber.getCountry()
                )
        );
    }

    @GetMapping(value = "/fullInfo")
    public ResponseEntity<PhoneNumber> getFullInfoAboutPhoneNumber(
            @RequestParam(required = false) Long numberId,
            @RequestParam(required = false) String number) {
        return ResponseEntity.ok(phoneNumberService.findPhoneNumberByIdOrNumber(numberId, number));

    }
}
