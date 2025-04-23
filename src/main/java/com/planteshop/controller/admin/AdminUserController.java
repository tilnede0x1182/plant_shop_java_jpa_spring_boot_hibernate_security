package com.planteshop.controller.admin;

import com.planteshop.model.entity.User;
import com.planteshop.model.enums.RoleType;
import com.planteshop.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

	private final UserRepository userRepository;

	public AdminUserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping
	public String index(Model model) {
		model.addAttribute("users", userRepository.findAll()
				.stream()
				.sorted((u1, u2) -> u1.getRole() == RoleType.ADMIN && u2.getRole() != RoleType.ADMIN ? -1
						: u1.getRole() != RoleType.ADMIN && u2.getRole() == RoleType.ADMIN ? 1 : 0)
				.toList());
		return "admin/users/index";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isEmpty())
			return "redirect:/admin/users";
		model.addAttribute("user", userOpt.get());
		return "admin/users/edit";
	}

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
			System.err.println("DEBUG : AdminUserController, @PatchMapping(\"/{id}\"), user : name - "+user.getName()+", email - "+user.getEmail()+", role - "+user.getRole());
			userRepository.save(user);
			User userTmp = userRepository.findById(id).orElse(null);
			if (userTmp != null) {
				System.err.println("DEBUG : AdminUserController, @PatchMapping(\"/{id}\"), userTmp : name - " + userTmp.getName() + ", email - " + userTmp.getEmail() + ", role - " + userTmp.getRole());
			}
		});
		return "redirect:/admin/users";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Long id) {
		userRepository.deleteById(id);
		return "redirect:/admin/users";
	}
}
