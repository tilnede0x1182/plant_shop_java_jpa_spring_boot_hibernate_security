# ───────────────────────────────
#   Compilation et exécution principales
# ───────────────────────────────

run:
	SPRING_PROFILES_ACTIVE=dev ./gradlew bootRun --console=plain

prod:
	@if [ -f ./jar/plant-shop.jar ]; then \
		java -jar ./jar/plant-shop.jar; \
	else \
		$(MAKE) build-jar; \
		java -jar ./jar/plant-shop.jar; \
	fi

prod-dev:
	rm -f ./jar/plant-shop.jar
	$(MAKE) prod

build: clean
	./gradlew clean compileJava

build-dev:
	$(MAKE) build
	$(MAKE) run

compile:
	./gradlew clean compileJava

compile_run: compile run

clean:
	./gradlew clean
	rm -rf build
	rm -rf jar
	rm -rf javadoc

javadoc:
	mkdir -p ./javadoc
	./gradlew clean javadoc
	if [ -d build/docs/javadoc ]; then cp -R build/docs/javadoc/. javadoc/; fi

build-jar:
	./gradlew clean syncProdJar

# ───────────────────────────────
#   Gestion de la seed
# ───────────────────────────────

seed:
	SPRING_PROFILES_ACTIVE=seed ./gradlew seed

compile-seed:
	SPRING_PROFILES_ACTIVE=seed ./gradlew clean compileJava

seed-dev:
	./gradlew clean seed

seed-build:
	SPRING_PROFILES_ACTIVE=seed ./gradlew clean bootJar -x test

# ───────────────────────────────
#   Gestion du test end-to-end
# ───────────────────────────────

tests:
	./gradlew clean test

compile-test:
	./gradlew testClasses

test-dev: compile-test tests

test-build:
	./gradlew clean build

# ───────────────────────────────
#   Commandes Base de données
# ───────────────────────────────

# Extrait la valeur depuis application.yml si disponible
# DB_NAME := $(shell sed -n 's/.*jdbc:postgresql:\/\/[^\/]*\/\([^?[:space:]]*\).*/\1/p' src/main/resources/application.yml | head -n 1 | tr -d '[:space:]')
DB_NAME = plant_shop_jpa_gradle

db-create:
	sudo -u postgres psql -c "CREATE DATABASE $(DB_NAME)" || echo "↳ base déjà existante."

db-migrate:
	sudo -u postgres psql -d $(DB_NAME) -f src/main/resources/db/migration/V1__init_schema.sql

db-drop:
	sudo -u postgres psql -c "DROP DATABASE IF EXISTS $(DB_NAME);"

db-reset: db-drop db-create db-migrate

# ───────────────────────────────
#   Utilitaires
# ───────────────────────────────

tree:
	tree . -I "build|.gradle|jar|javadoc"
