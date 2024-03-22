package com.zqhyz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "检活")
@RestController
@RequestMapping("/check")
public class CheckAliveController {

    @Operation(summary = "检活方法")
    @GetMapping("/alive")
    public String checkAlive() {
        return "alive";
    }
}
