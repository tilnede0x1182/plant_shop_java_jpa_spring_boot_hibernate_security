package com.planteshop.controller;

import com.planteshop.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserTypeAdvice {
	/**
	 * Détermine le rôle de l'utilisateur pour les vues Thymeleaf.
	 * Retourne visitor, user, ou admin selon l'authentification.
	 *
	 * @param authentication Authentication l'objet d'authentification Spring Security
	 * @return String le rôle de l'utilisateur en minuscules
	 */
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
