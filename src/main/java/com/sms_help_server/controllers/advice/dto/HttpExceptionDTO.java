package com.sms_help_server.controllers.advice.dto;

import com.sms_help_server.controllers.advice.HttpException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpExceptionDTO {
    private Integer code;
    private String message;

    public HttpExceptionDTO(HttpException exception) {
        this.code = exception.getStatus().value();
        this.message = exception.getMessage();
    }
}
