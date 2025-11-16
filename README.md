# Plant Shop - Plateforme Botanique Full-Stack

## 🌱 Présentation
Plant Shop est une application e-commerce complète pour vendre des plantes, construite en Spring Boot avec un frontend Thymeleaf. Le dépôt regroupe deux variantes (Gradle et Maven) partageant exactement la même base métier afin de rester DRY et d’explorer différentes chaînes de build Java modernes.

## 🎯 Objectif pédagogique
- Comprendre le fonctionnement de deux gestionnaires de dépendances (Gradle 8.10.2 et Maven 3+)
- Comparer leurs pipelines tout en conservant le même code métier, la même base PostgreSQL et les mêmes profils Spring
- Illustrer comment automatiser le cycle de vie (build, run, seed, packaging, DB) à travers des Makefiles dédiés

## 🧭 Organisation du dépôt
| Version | Répertoire | Gestionnaire | Build tool version | Base PostgreSQL par défaut |
| --- | --- | --- | --- | --- |
| Gradle | `plant_shop_java_jpa_spring_boot_hibernate_security_graddle` | Gradle Wrapper | 8.10.2 | `plant_shop_jpa_gradle` |
| Maven | `plant_shop_java_jpa_spring_boot_hibernate_security_maven` | Maven | 3.x (testé 3.9+) | `plant_shop_jpa_maven` |

## 🛠️ Stack partagée
- Java 21 · Spring Boot 3.1.5 · Spring Data JPA & Hibernate
- PostgreSQL 42.7.3 comme base de données relationnelle
- Spring Security 6 avec authentification par formulaire et sessions
- Jakarta Validation 3.0.2 + Hibernate Validator 8.0.1.Final
- Thymeleaf, Thymeleaf Layout Dialect 3.0.0 et Thymeleaf Spring Security 6 Extras
- Bootstrap 5.3.2 (WebJars) + WebJars Locator 0.46 + JavaScript vanilla pour le panier
- Lombok 1.18.30 et DataFaker 2.0.2 pour faciliter le développement

## 📦 Gestionnaires de dépendances : pourquoi deux variantes ?
- **Gradle** (Kotlin DSL) sert à démontrer la configuration déclarative moderne avec tâches customisées (`syncProdJar`, profils via variables d’environnement) et une génération de JAR production optimisée.
- **Maven** illustre l’approche conventionnelle et la transparence des étapes (plugins Spring Boot / Exec / Javadoc) tout en exposant la même application.
- Les deux Makefiles encapsulent les commandes pour faciliter la comparaison : chaque cible appelle le toolchain correspondant mais déclenche rigoureusement la même intention (build, seed, déploiement, base de données).

## 🚀 Commandes de démarrage
| Version | Build complet | Profil seed | Profil dev | Profil prod |
| --- | --- | --- | --- | --- |
| Gradle | `./gradlew clean build` | `SPRING_PROFILES_ACTIVE=seed ./gradlew bootRun` | `SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun` | `SPRING_PROFILES_ACTIVE=prod ./gradlew syncProdJar && java -jar jar/plant-shop.jar` |
| Maven | `mvn clean compile` | `mvn spring-boot:run -Dspring-boot.run.profiles=seed` | `mvn spring-boot:run -Dspring-boot.run.profiles=dev` | `mvn spring-boot:run -Dspring-boot.run.profiles=prod` |

## 🧰 Cibles Makefile essentielles
| Catégorie | Action | Variante Gradle | Variante Maven | Description |
| --- | --- | --- | --- | --- |
| Exécution | Dev rapide | `make run` | `make run` | Lance Spring Boot avec le profil `dev`.
| Exécution | Prod locale | `make prod` | `make prod` | Démarre le JAR (reconstruit si nécessaire).
| Exécution | Prod fresh | `make prod-dev` | `make prod-dev` | Purge le JAR puis relance la cible `prod`.
| Build | Compilation | `make build` | `make build` | Clean + compile (`compileJava` vs `mvn clean compile`).
| Build | Chaîne dev | `make build-dev` | `make build-dev` | Build puis exécution `run`.
| Build | Jar dédié | `make build-jar` | `make build-jar` | Produit/copier le JAR dans `./jar/plant-shop.jar`.
| Documentation | API JavaDoc | `make javadoc` | `make javadoc` | Génère et copie la doc dans `javadoc/`.
| Seed | Lancer seed | `make seed` | `make seed` | Exécute les scripts seed (profil `seed`).
| Seed | Build seed | `make seed-build` | `make seed-build` | Build/package en profil `seed`.
| Seed | Dev seed | `make seed-dev` | `make seed-dev` | Nettoie puis relance la seed.
| Base de données | Provision | `make db-create` | `make db-create` | Crée la base Postgres cible via `psql`.
| Base de données | Migration | `make db-migrate` | `make db-migrate` | Applique `src/main/resources/db/migration/migration.sql`.
| Base de données | Reset | `make db-reset` | `make db-reset` | Drop + create + migrate.
| Utilitaire | Arborescence | `make tree` | `make tree` | Affiche l’arbre en excluant les dossiers de build.

## 🧩 Fonctionnalités principales
- Catalogue des plantes, panier persistant côté client (localStorage) et tunnel de commande
- Profils utilisateurs avec historique d’achats et mise à jour du compte
- Administration des plantes (CRUD), des utilisateurs et interface dédiée aux rôles `ADMIN`

## 🛡️ Sécurité & Validation
- Authentification par formulaire, rôles `USER`/`ADMIN`, protection CSRF et contrôles d’accès Spring Security
- Validation systématique via Jakarta Validation + Hibernate Validator pour les données entrantes

## 🧪 Profils applicatifs
- `seed` : lance l’appli avec génération de données de test grâce à DataFaker
- `dev` : configuration locale orientée développement
- `prod` : paramètres optimisés production (couplé au JAR généré côté Gradle ou au run Maven)

## 📂 Structure logique
Les deux variantes suivent la même architecture MVC (controllers, services, repositories, entities) afin de garantir une séparation claire des responsabilités et d’éviter toute duplication métier.
