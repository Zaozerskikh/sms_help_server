package com.sms_help_server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminController {
    @RequestMapping("/hello")
    public String adminTest() {
        return "hello from admin";
    }
}
