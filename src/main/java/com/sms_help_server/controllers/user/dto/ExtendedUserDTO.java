package com.sms_help_server.controllers.user.dto;

import com.sms_help_server.entities.transaction.purchase.NumberPurchase;
import com.sms_help_server.entities.transaction.top_up.TopUp;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ExtendedUserDTO {
    private Long id;
    private Date registrationDate;
    private String email;
    private String nickname;
    private List<TopUp> topUps;
    private List<NumberPurchase> purchases;
}
