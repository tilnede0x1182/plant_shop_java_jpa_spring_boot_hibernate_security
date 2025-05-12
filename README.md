# Plant Shop - E-commerce Botanique (Spring Boot/Thymeleaf)

Application complète de vente de plantes développée avec Spring Boot et Thymeleaf :
- **Backend** : API REST avec Spring Boot et persistance JPA/Hibernate
- **Frontend** : Interface utilisateur avec Thymeleaf et Bootstrap

## 🛠 Stack Technique

### Backend
- **Langage**: Java 17
- **Framework**: Spring Boot 3.1.5
- **Persistence**:
  - Spring Data JPA
  - Hibernate (inclus dans Spring Boot)
  - PostgreSQL 42.7.3
- **Sécurité**:
  - Spring Security 6 (inclus dans Spring Boot 3.1.5)
  - Authentification par session
- **Validation**:
  - Jakarta Validation API 3.0.2
  - Hibernate Validator 8.0.1.Final
- **Utilitaires**:
  - Lombok 1.18.30
  - DataFaker 2.0.2 (pour les données de test)
- **Build**: Maven

### Frontend
- **Templates**:
  - Thymeleaf (via Spring Boot starter)
  - Thymeleaf Layout Dialect 3.0.0
  - Thymeleaf Spring Security 6 Extras
- **UI/UX**:
  - Bootstrap 5.3.2 (via WebJars)
  - WebJars Locator 0.46
  - JavaScript vanilla pour la gestion du panier

## Fonctionnalités

### Client
- Catalogue de plantes
- Panier d'achat (stockage côté client avec localStorage)
- Système de commande
- Profil utilisateur
- Historique des commandes

### Administrateur
- Gestion des plantes (CRUD)
- Gestion des utilisateurs
- Interface d'administration dédiée

### Sécurité
- Authentification via formulaire
- Roles utilisateur (USER/ADMIN)
- Protection CSRF
- Validation des données

## Installation et lancement

### Prérequis
- Java 17+
- PostgreSQL
- Maven

### Configuration
1. Créer une base de données PostgreSQL nommée `plant_shop_jpa`
2. Modifier les identifiants dans `application.yml` si nécessaire

### Démarrage
```bash
# Installation des dépendances
mvn clean compile

# Lancement avec données de test
mvn spring-boot:run -Dspring-boot.run.profiles=seed

# Lancement en mode développement
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Lancement en production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## Structure du projet
Architecture MVC classique avec séparation des couches controller, service, repository et entity.
