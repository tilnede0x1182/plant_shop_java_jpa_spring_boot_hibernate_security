# Définition des variables
PROFILE = dev
PORT = 8080

# Tâches par défaut
default: help

# Affiche l'aide
help:
	@echo "Commandes disponibles :"
	@echo "  make help          Affiche cette aide"
	@echo "  make run           Lance l'application (profil dev)"
	@echo "  make prod          Lance l'application (profil prod)"
	@echo "  make init          Lance l'application (profil init)"
	@echo "  make install       Compile le projet"
	@echo "  make test          Exécute les tests"
	@echo "  make clean         Supprime les fichiers générés"
	@echo "  make db-create     Crée la base de données"
	@echo "  make db-drop       Supprime la base de données"
	@echo "  make update        Met à jour le projet (alias reset)"
	@echo "  make reset         Réinitialise la base (alias init)"
	@echo "  make seed          Alias vers init"
	@echo "  make print_port    Affiche le port de l'application"

# Affichage du port
print_port:
	@echo "Port configuré : $(PORT)"

# Installaton des dépendances
install:
	clear
	@echo "Installation en cours..."
	mvn clean compile

# Lance l'application
run:
	clear
	@echo "Lancement en cours..."
	@$(MAKE) print_port
	# mvn -q spring-boot:run -Dspring-boot.run.profiles=$(PROFILE) | grep -E "WARN|ERROR|DEBUG :"
	mvn -q spring-boot:run -Dspring-boot.run.profiles=$(PROFILE)

prod: clean
	clear
	@echo "Lancement en cours..."
	@$(MAKE) print_port
	mvn -q spring-boot:run -Dspring-boot.run.profiles=prod | grep -E "WARN|ERROR"

init: print_port
	clear
	@echo "Lancement en cours..."
	@$(MAKE) print_port
	# mvn spring-boot:run -Dspring-boot.run.profiles=init | grep -E "WARN|ERROR|DEBUG :"
	mvn spring-boot:run -Dspring-boot.run.profiles=init

# Exécute les tests
test:
	clear && mvn -q test

# Nettoie les fichiers générés
clean:
	clear && mvn -q clean

# Crée les tables de la base de données
db-create: seed

# Supprime les tables de la base de données
db-drop:
	sudo -u postgres psql -d $(DB_NAME) -c "DROP SCHEMA public CASCADE;"
	sudo -u postgres psql -d $(DB_NAME) -c "CREATE SCHEMA public;"

# Met à jour les dépendances et reconstruit le projet
update: reset

# Réinitialise la base de données
reset: init

# Tâche pour seed la base de données (si vous avez un mécanisme de seed)
seed: init

.PHONY: test clean db-create db-drop update reset seed
