package com.sms_help_server.controllers.transaction_controller;

import com.sms_help_server.controllers.advice.HttpException;
import com.sms_help_server.controllers.transaction_controller.dto.CoinbaseChargeCreationDTO;
import com.sms_help_server.controllers.transaction_controller.dto.CoinbaseChargeDto;
import com.sms_help_server.controllers.transaction_controller.dto.CoinbaseChargeStatusDTO;
import com.sms_help_server.entities.transaction.crypto_top_up.CoinbaseCharge;
import com.sms_help_server.services.transaction_service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/transactions/")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<CoinbaseChargeDto> createCharge(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestBody CoinbaseChargeCreationDTO chargeCreationDto) {

        if (!Objects.equals(userId, chargeCreationDto.getUserId())) {
            throw new HttpException(HttpStatus.UNAUTHORIZED, "unathorized");
        }

        CoinbaseCharge charge = transactionService.createNewCharge(
                chargeCreationDto.getChargeId(),
                chargeCreationDto.getUserId(),
                chargeCreationDto.getAmount(),
                chargeCreationDto.getDescription()
        );

        return ResponseEntity.ok(new CoinbaseChargeDto(
                charge.getCoinbaseChargeId(),
                charge.getUser().getUserId(),
                charge.getTransactionStatus(),
                charge.getDescriptiton(),
                charge.getAmount()
        ));
    }

    @GetMapping("/refresh")
    public ResponseEntity<CoinbaseChargeStatusDTO> refreshCharge(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String email,
            @RequestParam String chargeId) {
        return ResponseEntity.ok(new CoinbaseChargeStatusDTO(
                chargeId,
                transactionService.refreshCharge(chargeId)
        ));
    }
}
