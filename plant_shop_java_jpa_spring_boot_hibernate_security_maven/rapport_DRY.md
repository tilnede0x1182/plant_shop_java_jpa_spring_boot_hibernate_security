# RAPPORT D'ANALYSE DES DUPLICATIONS DE CODE (Principe DRY)

## Introduction
Projet : `plant_shop_java_jpa_spring_boot_hibernate_security_maven` (Spring Boot + JPA). Objectif : pointer les répétitions bloquantes et les refactorings nécessaires.

---

## Violations DRY

### 1. même CRUD Plantes exposé 3 fois - 🔴 Critique
Les contrôleurs `ApiPlantController`, `WebPlantController` et `AdminPlantController` implémentent tous les mêmes opérations (liste, détail, création, mise à jour, suppression) en pilotant directement `PlantRepository`. Une modification de validation ou d’événement doit être répétée à trois endroits. **Action** : introduire un service applicatif (`PlantService`) contenant toute la logique métier; les contrôleurs REST/Web/Admin se contentent d’orchestrer vues ou DTO.

### 2. Mise à jour utilisateur recopiée - 🟠 Haute
`ProfileController` (profil personnel) et `AdminUserController` (panneau admin) dupliquent la même séquence : charger l’utilisateur, appliquer champs non vides, protéger le mot de passe existant, sauvegarder. Le moindre ajustement (nouvelle contrainte ou audit) doit être reporté deux fois. **Action** : déplacer cette logique dans un `UserService` ou un helper partagé (`applyUserChanges`), paramétré par les permissions (admin vs propriétaire).

### 3. Double codebase Maven/Gradle - 🟠 Haute
Ce module Maven contient la même arborescence `com.planteshop` que la variante Gradle voisine. Maintenir deux copies du code source viole frontalement DRY : corrections, seeds, tests et templates doivent être synchronisés manuellement. **Action** : mutualiser le code métier dans un module commun (jar) et ne conserver que des projets « enveloppes » Maven/Gradle, ou choisir un seul outil de build pour tout le backend.

---

## Impact estimé

| Refactoring proposé                      | Lignes supprimées | Complexité |
|------------------------------------------|-------------------|------------|
| Service unique pour les plantes          | ~200              | Faible     |
| Service partagé pour la mise à jour user | ~80               | Faible     |
| Fusion Maven/Gradle                      | code ×2 → ×1      | Moyenne    |

---

## Conclusion
Tant que les contrôleurs REST/web/admin et les deux piles Maven/Gradle restent indépendants, chaque évolution implique plusieurs modifications identiques. Centraliser la logique métier (services partagés) et supprimer la duplication inter-builds est indispensable pour respecter la règle DRY.***
