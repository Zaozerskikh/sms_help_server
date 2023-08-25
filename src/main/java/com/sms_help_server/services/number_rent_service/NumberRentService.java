package com.sms_help_server.services.number_rent_service;

import com.sms_help_server.controllers.info_controller.dto.CheckSmsPvaServiceInfoDTO;
import com.sms_help_server.entities.sms_pva_number_purchase.SmsPvaNumberPurchase;
import com.sms_help_server.services.number_rent_service.dto.SmsPvaGetServicePriceDTO;

public interface NumberRentService {
    SmsPvaNumberPurchase rentSmsPvaNumber(String serviceCode, Long userId);

    SmsPvaGetServicePriceDTO getSmsPvaGetServicePrice(String serviceCode);

    SmsPvaNumberPurchase checkSmsPvaCode(Long userId, Long purchseId);

    CheckSmsPvaServiceInfoDTO checkSmsPvaService(String serviceCode);
}
