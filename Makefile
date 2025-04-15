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

# Crée la base de données
db-create:
	psql -U postgres -c "CREATE DATABASE $(DB_NAME);"
	psql -U postgres -c "CREATE ROLE $(DB_USER) WITH PASSWORD '$(DB_PASSWORD)';"
	psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE $(DB_NAME) TO $(DB_USER);"

# Supprime la base de données
db-drop:
	psql -U postgres -c "DROP DATABASE IF EXISTS $(DB_NAME);"
	psql -U postgres -c "DROP ROLE IF EXISTS $(DB_USER);"

# Applique les migrations de base de données (si vous utilisez Flyway ou Liquibase)
db-migrate:
	mvn flyway:migrate -Dflyway.configFiles=src/main/resources/flyway.conf

# Nettoie les fichiers générés
clean:
	mvn clean

# Tâche pour seed la base de données (si vous avez un mécanisme de seed)
seed:
	mvn spring-boot:run -Dspring-boot.run.profiles=seed
