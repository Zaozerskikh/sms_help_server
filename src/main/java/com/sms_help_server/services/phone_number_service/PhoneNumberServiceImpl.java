package com.sms_help_server.services.phone_number_service;

import com.sms_help_server.controllers.admin_actions.dto.PhoneNumberRegistrationRequestDTO;
import com.sms_help_server.entities.phone_number.PhoneNumber;
import com.sms_help_server.entities.service_number_relation.ServiceNumberRelation;
import com.sms_help_server.repo.PhoneNumberRepository;
import com.sms_help_server.repo.ServiceRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {
    @Autowired
    private PhoneNumberRepository phoneNumberRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public PhoneNumber findPhoneNumberById(Long numberId) {
        return phoneNumberRepository
                .findByNumberId(numberId)
                .orElseThrow(() -> new NoSuchElementException("number with id " + numberId + " not found"));
    }

    @Override
    public PhoneNumber findPhoneNumberByNumber(String number) {
        return phoneNumberRepository
                .findByNumber(number)
                .orElseThrow(() -> new NoSuchElementException("phone number with number " + number + " not found"));
    }

    @Override
    public PhoneNumber findPhoneNumberByIdOrNumber(Long id, String number) {
        if (id == null && number == null) {
            throw new NoSuchElementException("neither id nor number was provided");
        }

        return  (id != null) ?
                this.findPhoneNumberById(id) :
                this.findPhoneNumberByNumber(number);
    }

    @Override
    @SneakyThrows
    public PhoneNumber registerNewPhoneNumber(PhoneNumberRegistrationRequestDTO phoneNumberRegistrationRequestDTO) {
        if (phoneNumberRepository.existsByNumber(phoneNumberRegistrationRequestDTO.getNumber())) {
            throw new NumberRegistrationException("number already registered");
        }

        if (phoneNumberRegistrationRequestDTO.getServiceIds() == null || phoneNumberRegistrationRequestDTO.getServiceIds().isEmpty()) {
            throw new NumberRegistrationException("no services provided.");
        }

        PhoneNumber newNumber = new PhoneNumber(
                phoneNumberRegistrationRequestDTO.getNumber(),
                phoneNumberRegistrationRequestDTO.getCountry()
        );

        newNumber.setServiceNumberRelations(
                phoneNumberRegistrationRequestDTO
                        .getServiceIds()
                        .stream()
                        .map(serviceId -> serviceRepository
                                .findById(serviceId)
                                .orElseThrow(() -> new RuntimeException("incorrect service id" + serviceId)))
                        .map(service -> new ServiceNumberRelation(service, newNumber))
                        .collect(Collectors.toList())
        );

        return phoneNumberRepository.save(newNumber);
    }
}
