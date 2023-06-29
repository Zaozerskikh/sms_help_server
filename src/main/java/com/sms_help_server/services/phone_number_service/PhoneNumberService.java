package com.sms_help_server.services.phone_number_service;

import com.sms_help_server.controllers.admin_actions.dto.PhoneNumberRegistrationRequestDTO;
import com.sms_help_server.entities.phone_number.PhoneNumber;

public interface PhoneNumberService {
    PhoneNumber findPhoneNumberById(Long numberId);

    PhoneNumber findPhoneNumberByNumber(String number);

    PhoneNumber findPhoneNumberByIdOrNumber(Long id, String number);

    PhoneNumber registerNewPhoneNumber(PhoneNumberRegistrationRequestDTO phoneNumberRegistrationRequestDTO);
}
