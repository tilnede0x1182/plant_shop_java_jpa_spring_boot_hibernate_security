#!/bin/bash

# Nettoyage et préparation
rm -rf target
mkdir -p src/main/resources/static

# Fichier de configuration
cat > src/main/resources/application.properties <<'EOF'
spring.datasource.url=jdbc:sqlite:file:./db/plant-shop.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.web.resources.static-locations=classpath:/static/
EOF

# Frontend React (index.html)
cat > src/main/resources/static/index.html <<'EOF'
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Plant Shop</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body { background-color: #f8f9fa; padding: 20px; }
    .plant-card {
      background: white;
      border: 1px solid #ddd;
      border-radius: 5px;
      padding: 15px;
      margin-bottom: 15px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
  </style>
</head>
<body>
  <div id="root"></div>

  <!-- React CDN -->
  <script crossorigin src="https://unpkg.com/react@18/umd/react.development.js"></script>
  <script crossorigin src="https://unpkg.com/react-dom@18/umd/react-dom.development.js"></script>
  <script src="https://unpkg.com/@babel/standalone/babel.min.js"></script>

  <!-- App React -->
  <script type="text/babel">
    function App() {
      const [plants, setPlants] = React.useState([]);

      React.useEffect(() => {
        fetch('/api/plants')
          .then(res => res.json())
          .then(setPlants);
      }, []);

      return (
        <div className="container">
          <h1 className="mb-4">Plant Shop</h1>
          <div className="row">
            {plants.map((plant, index) => (
              <div key={index} className="col-md-4">
                <div className="plant-card">
                  <h5>{plant.name}</h5>
                  <p>{plant.price} €</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      );
    }

    ReactDOM.createRoot(document.getElementById('root'))
      .render(<App />);
  </script>
</body>
</html>
EOF

# Mise à jour du contrôleur
cat > src/main/java/com/planteshop/controller/api/PlantController.java <<'EOF'
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
EOF

# Fichier POM.xml simplifié
cat > pom.xml <<'EOF'
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
            <groupId>org.xerial</groupId>
            <artifactId>sqlite-jdbc</artifactId>
            <version>3.42.0.0</version>
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

echo "Configuration terminée. Pour démarrer :"
echo "1. Assurez-vous que la base SQLite existe :"
echo "   mkdir -p db && touch db/plant-shop.db"
echo "2. Lancez l'application :"
echo "   mvn spring-boot:run"
echo "3. Accédez à : http://localhost:8080"
