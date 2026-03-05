package com.planteshop.controller.web;

import com.planteshop.model.entity.User;
import com.planteshop.model.enums.RoleType;
import com.planteshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructeur avec injection des dependances.
     *
     * @param userRepository UserRepository le repository des utilisateurs
     * @param passwordEncoder PasswordEncoder l'encodeur de mots de passe
     */
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Affiche le formulaire d'inscription.
     *
     * @param model Model le modele pour la vue
     * @return String le nom de la vue
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "authentification/register";
    }

    /**
     * Traite l'inscription d'un nouvel utilisateur.
     * Verifie l'unicite de l'email et encode le mot de passe.
     *
     * @param user User les donnees du formulaire
     * @param result BindingResult les erreurs de validation
     * @param model Model le modele pour la vue
     * @return String redirection vers login ou vue en cas d'erreur
     */
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            result.rejectValue("email", "error.user", "Cet email est déjà utilisé");
        }

        if (result.hasErrors()) {
            return "authentification/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(RoleType.USER);
        userRepository.save(user);

        return "redirect:/login?registered";
    }
}
