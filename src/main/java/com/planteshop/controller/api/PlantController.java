package com.planteshop.controller.api;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Arrays;

@RestController
@RequestMapping("/api/plants")
@CrossOrigin(origins = "*")
public class PlantController {

    @GetMapping
    public List<PlantResponse> getAllPlants() {
        // Simulation de données - À remplacer par un vrai accès SQLite
        return Arrays.asList(
            new PlantResponse("Ficus", 19.99),
            new PlantResponse("Cactus", 12.50)
        );
    }

    static class PlantResponse {
        public String name;
        public double price;

        public PlantResponse(String name, double price) {
            this.name = name;
            this.price = price;
        }
    }
}
