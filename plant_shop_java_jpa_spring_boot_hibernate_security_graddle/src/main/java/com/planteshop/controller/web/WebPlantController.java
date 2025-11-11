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

    public WebPlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/plants";
    }

    @GetMapping("/plants")
    public String index(Model model) {
				model.addAttribute("plants", plantRepository.findAll(Sort.by("name").ascending()));
        return "plants/index";
    }

    @GetMapping("/plants/{id}")
    public String show(@PathVariable Long id, Model model) {
        plantRepository.findById(id).ifPresent(plant -> model.addAttribute("plant", plant));
        return "plants/show";
    }
}
