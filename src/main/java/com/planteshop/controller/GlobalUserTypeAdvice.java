package com.planteshop.controller;

import com.planteshop.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserTypeAdvice {
	@ModelAttribute("userRole")
	public String userRole(Authentication authentication) {

			if (authentication == null                       // pas de session
					|| !authentication.isAuthenticated()         // pas encore validée
					|| authentication instanceof AnonymousAuthenticationToken) {
					return "visitor";
			}

			// on sait qu’on a notre entité User comme Principal
			User user = (User) authentication.getPrincipal();
			return user.getRole().name().toLowerCase();
	}
}
