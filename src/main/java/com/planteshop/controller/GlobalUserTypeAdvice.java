package com.planteshop.controller;

import com.planteshop.model.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserTypeAdvice {

    @ModelAttribute("finalCurrentUserType")
    public String finalCurrentUserType(@AuthenticationPrincipal User user) {
        if (user == null) return "visitor";
        return switch (user.getRole()) {
            case ADMIN -> "admin";
            case USER -> "user";
            default -> "visitor";
        };
    }
}
