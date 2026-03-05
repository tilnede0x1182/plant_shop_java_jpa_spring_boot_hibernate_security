package com.planteshop.controller.admin;

import com.planteshop.model.entity.Plant;
import org.springframework.data.domain.Sort;
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

	/**
	 * Constructeur avec injection du repository.
	 *
	 * @param plantRepository PlantRepository le repository des plantes
	 */
	public AdminPlantController(PlantRepository plantRepository) {
		this.plantRepository = plantRepository;
	}

	/**
	 * Affiche la liste des plantes triees par nom.
	 *
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue
	 */
	@GetMapping
	public String index(Model model) {
		model.addAttribute("plants", plantRepository.findAll(Sort.by("name").ascending()));
		return "admin/plants/index";
	}

	/**
	 * Affiche le formulaire de creation d'une nouvelle plante.
	 *
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue
	 */
	@GetMapping("/new")
	public String newPlant(Model model) {
		model.addAttribute("plant", new Plant());
		return "admin/plants/new";
	}

	/**
	 * Cree une nouvelle plante en base.
	 *
	 * @param plant Plant la plante a creer
	 * @return String redirection vers la liste
	 */
	@PostMapping
	public String create(@ModelAttribute Plant plant) {
		plantRepository.save(plant);
		return "redirect:/admin/plants";
	}

	/**
	 * Affiche le formulaire d'edition d'une plante.
	 *
	 * @param id Long l'identifiant de la plante
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue ou redirection si non trouvee
	 */
	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		Optional<Plant> plantOpt = plantRepository.findById(id);
		if (plantOpt.isEmpty())
			return "redirect:/admin/plants";
		model.addAttribute("plant", plantOpt.get());
		return "admin/plants/edit";
	}

	/**
	 * Met a jour une plante existante.
	 *
	 * @param id Long l'identifiant de la plante
	 * @param formPlant Plant les nouvelles donnees du formulaire
	 * @return String redirection vers la liste
	 */
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

	/**
	 * Supprime une plante par son identifiant.
	 *
	 * @param id Long l'identifiant de la plante a supprimer
	 * @return String redirection vers la liste
	 */
	@DeleteMapping("/{id}")
	public String delete(@PathVariable Long id) {
		plantRepository.deleteById(id);
		return "redirect:/admin/plants";
	}
}
