package com.planteshop.controller.api;

import com.planteshop.model.entity.Plant;
import com.planteshop.repository.PlantRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "*")
public class PlantController {

    private final PlantRepository plantRepository;

    public PlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @GetMapping
    public List<Plant> getAllPlants() {
        // Lecture réelle dans la base SQLite via le Repository
        return plantRepository.findAll();
    }

    @PostMapping
    public Plant addPlant(@RequestBody Plant plant) {
        // Pour ajouter une plante
        return plantRepository.save(plant);
    }

    // Autres méthodes REST (PUT, DELETE, etc.) si nécessaire
}
