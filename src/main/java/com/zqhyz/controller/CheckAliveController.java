package com.zqhyz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class CheckAliveController {
    @GetMapping("/alive")
    public String checkAlive() {
        return "alive";
    }
}
