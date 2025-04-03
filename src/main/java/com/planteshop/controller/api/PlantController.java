package com.planteshop.controller.api;

import com.planteshop.model.entity.Plant;
import com.planteshop.service.PlantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public List<Plant> getAll() {
        return plantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plant> getById(@PathVariable Long id) {
        Optional<Plant> plant = plantService.findById(id);
        return plant.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Plant> create(@RequestBody Plant plant) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(plantService.save(plant));
    }
}
