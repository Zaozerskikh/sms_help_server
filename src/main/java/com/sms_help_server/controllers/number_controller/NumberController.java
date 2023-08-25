package com.sms_help_server.controllers.number_controller;

import com.sms_help_server.controllers.number_controller.dto.CheckSmsResponseDTO;
import com.sms_help_server.controllers.number_controller.dto.NumberRentRequestDTO;
import com.sms_help_server.controllers.number_controller.dto.SmsPvaNumberRentResponseDTO;
import com.sms_help_server.entities.sms_pva_number_purchase.SmsPvaNumberPurchase;
import com.sms_help_server.services.number_rent_service.NumberRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/numbers/")
public class NumberController {
    @Autowired
    private NumberRentService numberRentService;

    @PostMapping("/smspva/rentNumber")
    public ResponseEntity<SmsPvaNumberRentResponseDTO> rentSmsPvaNumber(
        @RequestParam Long userId,
        @RequestBody NumberRentRequestDTO numberRentRequestDTO) {

        SmsPvaNumberPurchase purchase = numberRentService.rentSmsPvaNumber(
                numberRentRequestDTO.getServiceCode(),
                userId
        );

        return ResponseEntity.ok(
                new SmsPvaNumberRentResponseDTO(
                        purchase.getCountryCode().toString() + purchase.getNumber(),
                        purchase.getPurchaseId(), purchase.getUser().getBalance()
                )
        );
    }

    @PatchMapping("/smspva/checkCode")
    public ResponseEntity<CheckSmsResponseDTO> checkSmsPvaCode(
            @RequestParam Long userId,
            @RequestParam Long purchaseId) {
        SmsPvaNumberPurchase purchase = numberRentService.checkSmsPvaCode(userId, purchaseId);
        return ResponseEntity.ok(new CheckSmsResponseDTO(
                purchase.getCode() != null,
                purchase.getCode()
        ));
    }
}
