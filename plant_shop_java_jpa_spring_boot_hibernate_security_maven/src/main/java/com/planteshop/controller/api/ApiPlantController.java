package com.planteshop.controller.api;

import com.planteshop.model.entity.Plant;
import com.planteshop.repository.PlantRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class ApiPlantController {
    private final PlantRepository plantRepository;

    /**
     * Constructeur avec injection du repository.
     *
     * @param plantRepository PlantRepository le repository des plantes
     */
    public ApiPlantController(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    /**
     * Retourne la liste de toutes les plantes en JSON.
     *
     * @return List la liste des plantes
     */
    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    /**
     * Retourne une plante par son identifiant.
     *
     * @param id Long l'identifiant de la plante
     * @return Plant la plante ou null si non trouvée
     */
    @GetMapping("/{id}")
    public Plant getPlantById(@PathVariable Long id) {
        return plantRepository.findById(id).orElse(null);
    }

    /**
     * Crée une nouvelle plante via l'API.
     *
     * @param plant Plant la plante à créer
     * @return Plant la plante créée avec son identifiant
     */
    @PostMapping
    public Plant addPlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
    }

    /**
     * Met à jour une plante existante via l'API.
     *
     * @param id Long l'identifiant de la plante
     * @param plant Plant les nouvelles données
     * @return Plant la plante mise à jour
     */
    @PutMapping("/{id}")
    public Plant updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
        plant.setId(id);
        return plantRepository.save(plant);
    }

    /**
     * Supprime une plante via l'API.
     *
     * @param id Long l'identifiant de la plante à supprimer
     */
    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable Long id) {
        plantRepository.deleteById(id);
    }
}
