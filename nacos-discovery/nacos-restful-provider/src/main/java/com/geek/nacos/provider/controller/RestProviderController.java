package com.geek.nacos.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author geek
 */
@RestController
public class RestProviderController {

    @GetMapping("/service")
    public String service() {
        System.out.println("provider invoke...");
        return "provider invoke...";
    }

}
