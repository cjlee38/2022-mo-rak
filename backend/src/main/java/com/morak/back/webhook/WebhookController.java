package com.morak.back.webhook;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    @GetMapping
    public String hi() {
        return "웹훅의 첫째 아들";
    }
}
