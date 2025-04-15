# Définition des variables
APP_NAME = plant-shop-jpa-spring-boot-hibernate-security
DB_NAME = plant_shop
DB_USER = tilnede0x1182
DB_PASSWORD = tilnede0x1182
DB_HOST = localhost
DB_PORT = 5432
PROFILE = dev

# Tâches par défaut
default: help

# Affiche l'aide
help:
	@echo "Usage:"
	@echo "  make help         Affiche cette aide"
	@echo "  make run          Lance l'application"
	@echo "  make test         Exécute les tests"
	@echo "  make db-create    Crée la base de données"
	@echo "  make db-drop      Supprime la base de données"
	@echo "  make db-migrate   Applique les migrations de base de données"
	@echo "  make clean        Nettoie les fichiers générés"

# Lance l'application
run:
	mvn spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

# Exécute les tests
test:
	mvn test

# Nettoie les fichiers générés
clean:
	mvn clean

# Crée les tables de la base de données
db-create: seed

# Supprime les tables de la base de données
db-drop:
	sudo -u postgres psql -d $(DB_NAME) -c "DROP SCHEMA public CASCADE;"
	sudo -u postgres psql -d $(DB_NAME) -c "CREATE SCHEMA public;"

# Met à jour les dépendances et reconstruit le projet
update: reset

# Réinitialise la base de données
reset: seed

# Tâche pour seed la base de données (si vous avez un mécanisme de seed)
seed:
	mvn spring-boot:run -Dspring-boot.run.profiles=seed

.PHONY: test clean db-create db-drop update reset seed
