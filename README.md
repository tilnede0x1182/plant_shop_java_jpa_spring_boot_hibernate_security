# Plant Shop - E-commerce Botanique (JPA/Hibernate Edition)

Application complète de vente de plantes avec architecture modulaire :
- **Backend** : API REST sécurisée avec Spring Boot et persistance JPA/Hibernate
- **Frontend** : Interface React moderne avec gestion d'état

## 🛠 Stack Technique Complète

### Backend (Spring Boot)
- **Langage**: Java 17
- **Framework**: Spring Boot 3.1.5 + Spring MVC
- **Persistence**:
  - JPA 3.1 (Jakarta)
  - Hibernate 6.2
  - SQLite (Production) / H2 (Dev)
- **Sécurité**:
  - Spring Security 6
  - JWT Authentication
- **Build**: Maven
- **Templates**: Thymeleaf 3.1

### Frontend
- **Core**: React 18 (via CDN)
- **UI/UX**:
  - Bootstrap 5.3 (Thème personnalisé)

## Fonctionnalités Clés

### Sécurité
- Double couche JWT (Access + Refresh Tokens)
- Validation côté serveur
- Protection CSRF/CORS
