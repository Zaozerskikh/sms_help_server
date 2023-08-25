package com.sms_help_server.services.number_rent_service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms_help_server.controllers.info_controller.dto.CheckSmsPvaServiceInfoDTO;
import com.sms_help_server.entities.sms_pva_number_purchase.SmsPvaNumberPurchase;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import com.sms_help_server.repo.SmsPvaPurchaseRepository;
import com.sms_help_server.repo.UserRepository;
import com.sms_help_server.services.number_rent_service.dto.SmsPvaCheckServiceStockDTO;
import com.sms_help_server.services.number_rent_service.dto.SmsPvaGetNumberDTO;
import com.sms_help_server.services.number_rent_service.dto.SmsPvaGetServicePriceDTO;
import com.sms_help_server.services.number_rent_service.dto.SmsPvaGetSmsDTO;
import com.sms_help_server.services.number_rent_service.exception.SmsHelpException;
import com.sms_help_server.services.number_rent_service.exception.SmsPvaException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Log
@Service
public class NumberRentServiceImpl implements NumberRentService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SmsPvaPurchaseRepository smsPvaPurchaseRepository;

    @Value("${smspva.api_key}")
    private String SMS_PVA_API_KEY;

    @Value("${smspva.multiplier}")
    private double SMS_PVA_MULTIPLIER;

    @Value("${smspva.expiration}")
    private transient Integer EXPIRATION_IN_MINUTES;

    @Value("RU")
    private String country;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private final WebClient smsPvaWebClient = WebClient
            .builder()
            .baseUrl("https://smspva.com")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    @Override
    public SmsPvaNumberPurchase rentSmsPvaNumber(String serviceCode, Long userId) {
//        return smsPvaPurchaseRepository.findById(61L).orElseThrow();
        SmsHelpUser user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        SmsPvaGetServicePriceDTO priceInfo = getSmsPvaGetServicePrice(serviceCode);

        if (priceInfo == null || priceInfo.getResponse() != 1) {
            throw new SmsPvaException(HttpStatus.BAD_REQUEST, "smspva exception");
        }
        log.info("PRICE INFO: " + priceInfo);

        if (user.getBalance() - priceInfo.getPrice() < 0) {
            throw new SmsPvaException(HttpStatus.BAD_REQUEST, "Not enough money on balance to purchase");
        }

        SmsPvaGetNumberDTO numberInfo = smsPvaWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/priemnik.php")
                        .queryParam("metod", "get_number")
                        .queryParam("country", country)
                        .queryParam("service", serviceCode)
                        .queryParam("apikey", SMS_PVA_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(responseText -> {
                    try {
                        System.out.println(responseText);
                        return objectMapper.readValue(responseText.replace("CountryCode", "countryCode"), SmsPvaGetNumberDTO.class);
                    } catch (Exception e) {
                        throw new SmsPvaException(HttpStatus.BAD_REQUEST, e.getMessage());
                    }
                })
                .block();

        if (numberInfo == null || numberInfo.getNumber() == null || numberInfo.getResponse() != 1) {
            throw new SmsPvaException(HttpStatus.BAD_REQUEST, "smspva exception");
        }

        log.info("NUMBER INFO: " + numberInfo);
        double priceForUser = priceInfo.getPrice() * SMS_PVA_MULTIPLIER;

        log.info("PRICE FOR USER: " + priceForUser);
        user.increaseBalance(-priceForUser);
        userRepository.saveAndFlush(user);

        SmsPvaNumberPurchase purchase = new SmsPvaNumberPurchase(
                user, numberInfo.getId(), serviceCode, numberInfo.getNumber(), numberInfo.getCountry(),
                Integer.parseInt(numberInfo.getCountryCode()), priceInfo.getPrice(), priceForUser, EXPIRATION_IN_MINUTES
        );
        return smsPvaPurchaseRepository.saveAndFlush(purchase);
    }

    @Override
    public SmsPvaNumberPurchase checkSmsPvaCode(Long userId, Long purchseId) {
        SmsPvaNumberPurchase purchase = smsPvaPurchaseRepository
                .findById(purchseId)
                .orElseThrow(() -> new SmsHelpException(HttpStatus.BAD_REQUEST, "SmsPva purchase with this id not found"));

        if (!Objects.equals(purchase.getUser().getUserId(), userId)) {
            throw new SmsHelpException(HttpStatus.FORBIDDEN, "get lost");
        }

        if (purchase.getCode() != null) {
            return purchase;
        }

        SmsPvaGetSmsDTO codeInfo = smsPvaWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/priemnik.php")
                        .queryParam("metod", "get_sms")
                        .queryParam("country", purchase.getCountry())
                        .queryParam("service", purchase.getServiceCode())
                        .queryParam("id", purchase.getSmsPvaPurchaseId())
                        .queryParam("apikey", SMS_PVA_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(responseText -> {
                    try {
                        log.info(responseText);
                        return objectMapper.readValue(responseText, SmsPvaGetSmsDTO.class);
                    } catch (Exception e) {
                        throw new SmsPvaException(HttpStatus.BAD_REQUEST, e.getMessage());
                    }
                })
                .block();

        if (codeInfo == null) {
            throw new SmsPvaException(HttpStatus.BAD_REQUEST, "smspva exception");
        }
        log.info(codeInfo.toString());

        if (codeInfo.getResponse() == 1 && codeInfo.getSms() != null) {
            purchase.setCode(codeInfo.getSms());
            return this.smsPvaPurchaseRepository.saveAndFlush(purchase);
        }
        return purchase;
    }

    @Override
    public SmsPvaGetServicePriceDTO getSmsPvaGetServicePrice(String serviceCode) {
        return smsPvaWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/priemnik.php")
                        .queryParam("metod", "get_service_price")
                        .queryParam("country", country)
                        .queryParam("service", serviceCode)
                        .queryParam("apikey", SMS_PVA_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(responseText -> {
                    try {
                        return objectMapper.readValue(responseText, SmsPvaGetServicePriceDTO.class);
                    } catch (Exception e) {
                        throw new SmsPvaException(HttpStatus.BAD_REQUEST, e.getMessage());
                    }
                })
                .block();
    }

    @Override
    public CheckSmsPvaServiceInfoDTO checkSmsPvaService(String serviceCode) {
        SmsPvaCheckServiceStockDTO stockInfo = smsPvaWebClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/priemnik.php")
                        .queryParam("metod", "get_count_new")
                        .queryParam("country", country)
                        .queryParam("service", serviceCode)
                        .queryParam("apikey", SMS_PVA_API_KEY)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(responseText -> {
                    try {
                        return objectMapper.readValue(responseText, SmsPvaCheckServiceStockDTO.class);
                    } catch (Exception e) {
                        throw new SmsPvaException(HttpStatus.BAD_REQUEST, e.getMessage());
                    }
                })
                .block();

        if (stockInfo == null || stockInfo.getOnline() <= 0) {
            return new CheckSmsPvaServiceInfoDTO(serviceCode, null, SMS_PVA_MULTIPLIER, false);
        }

        SmsPvaGetServicePriceDTO priceInfo = getSmsPvaGetServicePrice(serviceCode);

        if (priceInfo != null) {
            return new CheckSmsPvaServiceInfoDTO(serviceCode, priceInfo.getPrice(), SMS_PVA_MULTIPLIER, true);
        }

        return new CheckSmsPvaServiceInfoDTO(serviceCode, null, SMS_PVA_MULTIPLIER, false);
    }
}
