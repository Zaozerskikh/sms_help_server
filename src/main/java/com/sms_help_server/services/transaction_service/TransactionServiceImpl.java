package com.sms_help_server.services.transaction_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sms_help_server.entities.transaction.TransactionStatus;
import com.sms_help_server.entities.transaction.coinbase_top_up.CoinbaseCharge;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import com.sms_help_server.repo.CoinbaseChargeRepository;
import com.sms_help_server.security.exceptions.TransactionException;
import com.sms_help_server.services.user_service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Value("${coinbase.url}")
    private String coinbaseUrl;

    @Value("${coinbase.api_key}")
    private String coinbaseApiKey;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinbaseChargeRepository chargeRepository;

    @Override
    public CoinbaseCharge createNewCharge(String chargeId, Long userId, Double amount, String description) {
        if (chargeRepository.existsByCoinbaseChargeId(chargeId)) {
            throw new TransactionException(HttpStatus.BAD_REQUEST, "Charge with this id already exists.");
        }

        getCoinbaseCharge(chargeId);

        CoinbaseCharge charge = new CoinbaseCharge(
                chargeId, userService.findById(userId), description, amount
        );

        return chargeRepository.saveAndFlush(charge);
    }

    @SneakyThrows
    @Override
    public TransactionStatus refreshCharge(String chargeId) {
        String response = getCoinbaseCharge(chargeId);
        JsonNode chargeNode = new ObjectMapper().readTree(response);

        var lastStatus = StreamSupport
                .stream(chargeNode.get("data").get("timeline").spliterator(), false)
                .sorted(Comparator.comparing(node -> Date.from(Instant.parse(node.get("time").textValue())), Comparator.reverseOrder()))
                .map(node -> node.get("status").textValue())
                .collect(Collectors.toList())
                .get(0);

        if (lastStatus.equalsIgnoreCase(CoinbaseChargeStatus.COMPLETED.name())) {
            CoinbaseCharge charge = chargeRepository
                    .findByCoinbaseChargeId(chargeId)
                    .orElseThrow(() -> new TransactionException(HttpStatus.NOT_FOUND, "Charge doesn't exists."));

            if (charge.getTransactionStatus() == TransactionStatus.COMPLETED) {
                throw new TransactionException(HttpStatus.BAD_REQUEST, "Unable to refresh. Charge has been already completed.");
            } else {
                SmsHelpUser user = charge.getUser();
                user.increaseBalance(charge.getAmount());
                charge.setTransactionStatus(TransactionStatus.COMPLETED);
                chargeRepository.saveAndFlush(charge);
                return TransactionStatus.COMPLETED;
            }
        }
        return TransactionStatus.IN_PROGRESS;
    }

    private String getCoinbaseCharge(String chargeId) {
        return WebClient
                .builder()
                .baseUrl(coinbaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-CC-Api-Key", coinbaseApiKey)
                .defaultHeader("X-CC-Version", "2018-03-22")
                .build()
                .get()
                .uri("/{chargeId}", chargeId)
                .exchangeToMono(response -> {
                    if (response.statusCode().equals(HttpStatus.OK)) {
                        return response.bodyToMono(String.class);
                    } else if (response.statusCode().equals(HttpStatus.NOT_FOUND)) {
                        throw new TransactionException(HttpStatus.NOT_FOUND, "Charge not found.");
                    } else {
                        throw new TransactionException(HttpStatus.BAD_REQUEST, "Unknown error.");
                    }
                })
                .block();
    }
}
