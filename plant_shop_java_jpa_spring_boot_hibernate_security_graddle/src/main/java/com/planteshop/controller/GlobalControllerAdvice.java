// src/main/java/com/planteshop/controller/GlobalControllerAdvice.java
package com.planteshop.controller;

import com.planteshop.util.StringUtils;
import com.planteshop.model.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
		/**
		 * Fournit le nom de l'utilisateur connecte formate pour l'affichage dans les vues.
		 *
		 * @param user User l'utilisateur authentifie injecte par Spring Security
		 * @return String le nom formate ou null si non connecte
		 */
		@ModelAttribute("loggedUserName")
		public String loggedUserName(@AuthenticationPrincipal User user) {
				return (user != null) ? StringUtils.toCapitalCase(user.getName()) : null;
		}
}
