package com.planteshop.controller.admin;

import com.planteshop.model.entity.Plant;
import com.planteshop.repository.PlantRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@Controller
@RequestMapping("/admin/plants")
public class AdminPlantController {

	private final PlantRepository plantRepository;

	public AdminPlantController(PlantRepository plantRepository) {
		this.plantRepository = plantRepository;
	}

	@GetMapping
	public String index(Model model) {
		model.addAttribute("plants", plantRepository.findAll());
		return "admin/plants/index";
	}

	@GetMapping("/new")
	public String newPlant(Model model) {
		model.addAttribute("plant", new Plant());
		return "admin/plants/new";
	}

	@PostMapping
	public String create(@ModelAttribute Plant plant) {
		plantRepository.save(plant);
		return "redirect:/admin/plants";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Optional<Plant> plantOpt = plantRepository.findById(id);
		if (plantOpt.isEmpty())
			return "redirect:/admin/plants";
		model.addAttribute("plant", plantOpt.get());
		return "admin/plants/edit";
	}

	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute Plant formPlant) {
		plantRepository.findById(id).ifPresent(plant -> {
			plant.setName(formPlant.getName());
			plant.setPrice(formPlant.getPrice());
			plant.setDescription(formPlant.getDescription());
			plant.setStock(formPlant.getStock());
			plant.setCategory(formPlant.getCategory());
			plantRepository.save(plant);
		});
		return "redirect:/admin/plants";
	}

	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {
		plantRepository.deleteById(id);
		return "redirect:/admin/plants";
	}
}
