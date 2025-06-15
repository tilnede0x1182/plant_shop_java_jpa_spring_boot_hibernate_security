package com.planteshop.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "authentification/login"; // Correspond à src/main/resources/templates/login.html
    }
}
