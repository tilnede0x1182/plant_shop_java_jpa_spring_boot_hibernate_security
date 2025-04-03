#!/bin/bash

# Création de l'arborescence du projet
mkdir -p plant-shop-backend/src/{main/{java/com/planteshop/{config,controller/{api,exception},model/{entity,dto/{request,response},enums},repository,service/impl,util},resources/db/migration},test/java/com/planteshop/{controller,service}}

# Fichier principal Spring Boot
cat > plant-shop-backend/src/main/java/com/planteshop/PlantShopApplication.java <<'EOF'
package com.planteshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PlantShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlantShopApplication.class, args);
    }
}
EOF

# Configuration Spring Security
cat > plant-shop-backend/src/main/java/com/planteshop/config/SecurityConfig.java <<'EOF'
package com.planteshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .httpBasic();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
EOF

# Entité Plante
cat > plant-shop-backend/src/main/java/com/planteshop/model/entity/Plant.java <<'EOF'
package com.planteshop.model.entity;

import javax.persistence.*;

@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer stock;

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
EOF

# Repository Plante
cat > plant-shop-backend/src/main/java/com/planteshop/repository/PlantRepository.java <<'EOF'
package com.planteshop.repository;

import com.planteshop.model.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
EOF

# Contrôleur Plante
cat > plant-shop-backend/src/main/java/com/planteshop/controller/api/PlantController.java <<'EOF'
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
EOF

# Fichier de configuration application.properties
cat > plant-shop-backend/src/main/resources/application.properties <<'EOF'
spring.datasource.url=jdbc:sqlite:file:./db/plant-shop.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
EOF

# Script SQL d'initialisation
cat > plant-shop-backend/src/main/resources/db/migration/V1__init_schema.sql <<'EOF'
CREATE TABLE IF NOT EXISTS plant (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    description TEXT,
    price REAL NOT NULL,
    category TEXT,
    stock INTEGER DEFAULT 0
);

CREATE TABLE IF NOT EXISTS app_user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    address TEXT,
    phone TEXT,
    active BOOLEAN NOT NULL DEFAULT true,
    registration_date TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
);
EOF

# Fichier POM.xml
cat > plant-shop-backend/pom.xml <<'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
    </parent>

    <groupId>com.planteshop</groupId>
    <artifactId>plant-shop</artifactId>
    <version>1.0.0</version>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.42.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Fichier .gitignore
cat > plant-shop-backend/.gitignore <<'EOF'
*.class
*.log
*.sqlite
/target/
/db/
.idea/
.vscode/
*.iml
.DS_Store
EOF

echo "Projet Spring Boot généré avec succès dans le répertoire plant-shop-backend"
echo "Pour démarrer : mvn spring-boot:run"
