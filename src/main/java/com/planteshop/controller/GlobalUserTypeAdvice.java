package com.planteshop.controller;

import com.planteshop.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalUserTypeAdvice {
	/**
	 * Expose l’énum directement à la vue.
	 * - VISITOR si pas connecté
	 * - USER / ADMIN sinon
	 */
	// @ModelAttribute("userRole")
	// public String userRole(@AuthenticationPrincipal User user) {
	// 	try {
	// 		return user.getRole().name().toLowerCase();
	// 	} catch (Exception exception) {
	// 		System.err.println("DEBUG : GlobalUserTypeAdvice.serRole - Error : "+exception);
	// 		return "visitor";
	// 	}
	// }

	// @ModelAttribute("userRole")
	// public String userRole(@AuthenticationPrincipal User user) {
	// 		return user.getRole().name().toLowerCase();
	// }

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
