#!/bin/bash

# Script complet de correction pour le projet PlantShop
echo "🔧 Début de la correction complète..."

# 1. Mise à jour de la configuration de sécurité
SECURITY_CONFIG="src/main/java/com/planteshop/config/SecurityConfig.java"
echo "🛡️  Mise à jour de la sécurité dans $SECURITY_CONFIG"

cat > "$SECURITY_CONFIG" << 'EOL'
package com.planteshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/**").permitAll()  // Accès complet sans authentification
            );
        return http.build();
    }
}
EOL

# 2. Nettoyage des propriétés redondantes
APP_PROPERTIES="src/main/resources/application.properties"
echo "🧹 Nettoyage de $APP_PROPERTIES"

# Garde uniquement les configurations essentielles
cat > "$APP_PROPERTIES" << 'EOL'
spring.datasource.url=jdbc:postgresql://localhost:5432/plant_shop
spring.datasource.username=tilnede0x1182
spring.datasource.password=tilnede0x1182
spring.jpa.hibernate.ddl-auto=update

# Configuration CORS globale
spring.web.resources.static-locations=classpath:/static/
EOL

# 3. Suppression des fichiers cibles
echo "🧹 Nettoyage des fichiers compilés"
rm -rf target/

# 4. Reconstruction du projet
echo "🏗️  Reconstruction de l'application..."
mvn clean package

echo "🚀 Lancement de l'application..."
mvn spring-boot:run

echo "✅ Corrections appliquées avec succès !"
echo "➡️ L'application devrait maintenant :"
echo "- Ne plus demander d'authentification"
echo "- Être accessible sans restriction"
echo "- Fonctionner avec PostgreSQL"
