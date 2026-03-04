package com.planteshop.controller.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.planteshop.repository.UserRepository;

import com.planteshop.model.entity.User;
import com.planteshop.util.StringUtils;

@Controller
public class ProfileController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * Constructeur avec injection des dépendances.
	 *
	 * @param userRepository UserRepository le repository des utilisateurs
	 * @param passwordEncoder PasswordEncoder l'encodeur de mots de passe
	 */
	public ProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
	}

    /**
     * Affiche le profil de l'utilisateur connecté.
     *
     * @param user User l'utilisateur authentifié
     * @param model Model le modèle Thymeleaf
     * @return String le nom de la vue
     */
    @GetMapping("/my_profile")
    public String profile(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("displayName", StringUtils.toCapitalCase(user.getName()));
        return "users/show";
    }

		/** Formulaire d’édition */
		@GetMapping("/my_profile/edit")
    public String edit(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);        // pré‑remplit le formulaire
        return "users/edit";
    }

    /** Traitement du formulaire */
		@PostMapping("/my_profile/edit")
		public String update(@ModelAttribute("user") User formUser,
												 BindingResult result,
												 @AuthenticationPrincipal User sessionUser) {
				if (result.hasErrors()) {
						return "profile/profile_edit";
				}

				sessionUser.setName(formUser.getName());
				sessionUser.setEmail(formUser.getEmail());

				if (formUser.getPassword() != null && !formUser.getPassword().isBlank()) {
						sessionUser.setPassword(passwordEncoder.encode(formUser.getPassword()));
				}

				userRepository.save(sessionUser);
				return "redirect:/my_profile?updated";
		}
}
