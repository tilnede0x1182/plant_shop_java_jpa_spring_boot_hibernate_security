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
     * Retourne la liste de toutes les plantes.
     *
     * @return List<Plant> la liste des plantes
     */
    @GetMapping
    public List<Plant> getAllPlants() {
        return plantRepository.findAll();
    }

    /**
     * Retourne une plante par son identifiant.
     *
     * @param id Long l'identifiant de la plante
     * @return Plant la plante ou null si non trouvee
     */
    @GetMapping("/{id}")
    public Plant getPlantById(@PathVariable Long id) {
        return plantRepository.findById(id).orElse(null);
    }

    /**
     * Cree une nouvelle plante.
     *
     * @param plant Plant la plante a creer
     * @return Plant la plante creee avec son identifiant
     */
    @PostMapping
    public Plant addPlant(@RequestBody Plant plant) {
        return plantRepository.save(plant);
    }

    /**
     * Met a jour une plante existante.
     *
     * @param id Long l'identifiant de la plante
     * @param plant Plant les nouvelles donnees
     * @return Plant la plante mise a jour
     */
    @PutMapping("/{id}")
    public Plant updatePlant(@PathVariable Long id, @RequestBody Plant plant) {
        plant.setId(id);
        return plantRepository.save(plant);
    }

    /**
     * Supprime une plante par son identifiant.
     *
     * @param id Long l'identifiant de la plante a supprimer
     */
    @DeleteMapping("/{id}")
    public void deletePlant(@PathVariable Long id) {
        plantRepository.deleteById(id);
    }
}
