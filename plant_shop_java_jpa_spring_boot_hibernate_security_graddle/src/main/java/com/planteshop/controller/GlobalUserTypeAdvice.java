package com.planteshop.controller;

import com.planteshop.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserTypeAdvice {
	/**
	 * Determine le role de l'utilisateur courant pour les vues.
	 * Retourne "visitor" si non authentifie, sinon le role en minuscules.
	 *
	 * @param authentication Authentication l'objet d'authentification Spring Security
	 * @return String le role de l'utilisateur (visitor, user, admin)
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
