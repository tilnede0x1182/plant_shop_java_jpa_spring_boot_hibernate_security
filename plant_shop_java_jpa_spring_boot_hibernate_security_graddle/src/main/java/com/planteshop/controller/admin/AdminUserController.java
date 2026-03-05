package com.planteshop.controller.admin;

import com.planteshop.model.entity.User;
import com.planteshop.model.enums.RoleType;
import com.planteshop.repository.UserRepository;
import jakarta.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.Comparator;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

	private final UserRepository userRepository;

	/**
	 * Constructeur avec injection du repository.
	 *
	 * @param userRepository UserRepository le repository des utilisateurs
	 */
	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/**
	 * Affiche la liste des utilisateurs tries par role puis par nom.
	 *
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue
	 */
	@GetMapping
	public String index(Model model) {
		model.addAttribute("users", userRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(User::getRole, Comparator.comparing(role -> role != RoleType.ADMIN))
						.thenComparing(User::getName, String.CASE_INSENSITIVE_ORDER))
				.toList());
		return "admin/users/index";
	}

	/**
	 * Affiche le formulaire d'edition d'un utilisateur.
	 *
	 * @param id Long l'identifiant de l'utilisateur
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue ou redirection si non trouve
	 */
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isEmpty())
			return "redirect:/admin/users";
		model.addAttribute("user", userOpt.get());
		return "admin/users/edit";
	}

	/**
	 * Met a jour un utilisateur existant.
	 * Preserve le mot de passe existant et invalide la session.
	 *
	 * @param id Long l'identifiant de l'utilisateur
	 * @param formUser User les nouvelles donnees du formulaire
	 * @return String redirection vers la liste
	 */
	@PatchMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute User formUser) {
		userRepository.findById(id).ifPresent(user -> {
			if (formUser.getEmail() != null && !formUser.getEmail().isBlank()) {
				user.setEmail(formUser.getEmail());
			}
			if (formUser.getName() != null && !formUser.getName().isBlank()) {
				user.setName(formUser.getName());
			}
			if (formUser.getRole() != null) {
				user.setRole(formUser.getRole());
			}
			// Récupéreration de l'ancien mot de passe depuis la base de données
			// et réattribution pour empêcher Hibernate de le mettre à NULL
			User existingUser = userRepository.findById(id).orElseThrow();
			user.setPassword(existingUser.getPassword());

			userRepository.save(user);
			User userTmp = userRepository.findById(id).orElse(null);
		});
		SecurityContextHolder.clearContext();
		return "redirect:/admin/users";
	}

	/**
	 * Supprime un utilisateur par son identifiant.
	 *
	 * @param id Long l'identifiant de l'utilisateur a supprimer
	 * @return String redirection vers la liste
	 */
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {
		userRepository.deleteById(id);
		return "redirect:/admin/users";
	}
}
