# RAPPORT D'ANALYSE DES DUPLICATIONS DE CODE (Principe DRY)

## Introduction
Projet : `plant_shop_java_jpa_spring_boot_hibernate_security_graddle` (Spring Boot + JPA). Objectif : recenser les entorses DRY et proposer des refactorings concrets.

---

## Violations DRY

### 1. CRUD Plantes recopié dans 3 contrôleurs - 🔴 Critique
- API REST : `ApiPlantController` expose `GET/POST/PUT/DELETE` en manipulant directement `PlantRepository`.
- Front public : `WebPlantController` exécute les mêmes requêtes (liste triée, show).
- Interface admin : `AdminPlantController` répète toute la logique de création, édition, suppression (copie quasi mot à mot de l’API).

Tout changement métier (validation, calcul du stock, mapping DTO) doit être appliqué à trois classes. **Action** : introduire un `PlantService` (ou cas d’usage) unique appelé par les contrôleurs (REST, web public, admin). Les contrôleurs ne gèrent alors que l’adaptation HTTP/Thymeleaf.

### 2. Mise à jour utilisateur dupliquée (profil vs admin) - 🟠 Haute
- `ProfileController` autorise l’utilisateur courant à modifier nom, email et mot de passe (lignes 26-57).
- `AdminUserController` réalise la même mise à jour (champs conditionnels, protection du password) pour l’admin (lignes 25-66).

Les deux blocs contiennent les mêmes vérifications (`isBlank`, recopie du mot de passe existant). **Action** : créer une méthode partagée (ex. `UserService.updateUser(User target, UserChanges dto, boolean allowRoleChange)`) réutilisée par les deux contrôleurs; déplacer également la logique de sauvegarde + rafraîchissement de session.

### 3. Code source dupliqué entre variantes Gradle/Maven - 🟠 Haute
Le même backend est maintenu dans deux dossiers (`..._graddle` et `..._maven`) avec exactement les mêmes packages `com.planteshop`. Toute correction (contrôleur, seed, templates) doit être portée à deux copies manuelles, ce qui viole gravement DRY. **Action** : mutualiser le code dans un module unique (par exemple `plant_shop_java_jpa`) et ne conserver qu’une configuration Gradle/Maven minimale (ou utiliser Gradle pour générer un wrapper Maven). À défaut, factoriser via un sous-module commun importé par les deux builds afin d’éviter la duplication du code métier.

---

## Impact estimé

| Refactoring proposé                               | Lignes éliminées | Complexité |
|---------------------------------------------------|------------------|------------|
| Service unique pour la gestion des plantes        | ~200             | Faible     |
| Service partagé pour la mise à jour des utilisateurs | ~80          | Faible     |
| Mutualisation Gradle/Maven                        | code ×2 → ×1     | Moyenne    |

---

## Conclusion
Sans couche de service partagée, chaque fonctionnalité (CRUD plantes, gestion utilisateurs) est réécrite plusieurs fois (REST, pages publiques, admin). Ajouter un service métier et fusionner les variantes Gradle/Maven est indispensable pour respecter la consigne DRY et réduire drastiquement les risques de divergence.***
