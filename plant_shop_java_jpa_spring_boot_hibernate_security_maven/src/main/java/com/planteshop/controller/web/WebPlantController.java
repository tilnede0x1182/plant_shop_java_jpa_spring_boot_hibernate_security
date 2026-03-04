package com.planteshop.controller.web;

import com.planteshop.repository.PlantRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebPlantController {
    private final PlantRepository plantRepository;

    /**
     * Constructeur avec injection du repository.
     *
     * @param plantRepository PlantRepository le repository des plantes
     */
    public WebPlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    /**
     * Page d'accueil, redirige vers la liste des plantes.
     *
     * @return String redirection vers /plants
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/plants";
    }

    /**
     * Affiche la liste des plantes triées par nom.
     *
     * @param model Model le modèle Thymeleaf
     * @return String le nom de la vue
     */
    @GetMapping("/plants")
    public String index(Model model) {
				model.addAttribute("plants", plantRepository.findAll(Sort.by("name").ascending()));
        return "plants/index";
    }

    /**
     * Affiche le détail d'une plante.
     *
     * @param id Long l'identifiant de la plante
     * @param model Model le modèle Thymeleaf
     * @return String le nom de la vue
     */
    @GetMapping("/plants/{id}")
    public String show(@PathVariable Long id, Model model) {
        plantRepository.findById(id).ifPresent(plant -> model.addAttribute("plant", plant));
        return "plants/show";
    }
}
