package com.sms_help_server.controllers.info_controller;


import com.sms_help_server.controllers.info_controller.dto.CheckSmsPvaServiceInfoDTO;
import com.sms_help_server.services.number_rent_service.NumberRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/info/")
public class InfoController {
    @Autowired
    private NumberRentService numberRentService;

    @GetMapping("/smspva/checkService")
    public ResponseEntity<CheckSmsPvaServiceInfoDTO> checkSmsPvaServiceInfo(
            @RequestParam String serviceCode) {
        return ResponseEntity.ok(numberRentService.checkSmsPvaService(serviceCode));
    }
}
