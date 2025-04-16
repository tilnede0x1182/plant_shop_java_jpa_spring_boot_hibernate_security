package com.planteshop.controller.web;

import com.planteshop.model.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    @GetMapping("/my_profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "profile";
    }
}
