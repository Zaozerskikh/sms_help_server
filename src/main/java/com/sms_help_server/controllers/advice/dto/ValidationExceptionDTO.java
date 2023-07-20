package com.sms_help_server.controllers.advice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationExceptionDTO extends HttpExceptionDTO {
    private String entityClass;
    private Map<String, String> errors;

    public ValidationExceptionDTO(int code, String message, String entityClass, Map<String, String> errors) {
        super(code, message);
        this.entityClass = entityClass;
        this.errors = errors;
    }
}
