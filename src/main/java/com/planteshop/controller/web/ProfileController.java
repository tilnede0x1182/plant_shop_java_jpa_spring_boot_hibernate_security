package com.planteshop.controller.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.planteshop.model.entity.User;
import com.planteshop.util.StringUtils;

@Controller
public class ProfileController {

    @GetMapping("/my_profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("displayName", StringUtils.toCapitalCase(user.getName()));
        return "profile";
    }
}
