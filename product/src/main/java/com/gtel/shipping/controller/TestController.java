package com.gtel.shipping.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/test")
@Slf4j
public class TestController {

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String sayHello() {
        return "Hello";
    }

}
