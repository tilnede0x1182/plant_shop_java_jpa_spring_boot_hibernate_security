# ───────────────────────────────
#   Compilation et exécution principales
# ───────────────────────────────

run:
	mvn spring-boot:run -Dspring-boot.run.profiles=dev

prod:
	if [ -f ./jar/plant-shop.jar ]; then \
		java -jar ./jar/plant-shop.jar; \
	else \
		$(MAKE) build-jar; \
		java -jar ./jar/plant-shop.jar; \
	fi

prod-dev:
	rm -f ./jar/plant-shop.jar
	$(MAKE) prod

build: clean
	if [ -d target ]; then rm -f $$(find target -name '*.class' -type f); fi
	mvn clean compile

build-dev:
	if [ -d target ]; then rm -f $$(find target -name '*.class' -type f); fi
	$(MAKE) build
	$(MAKE) run

compile:
	mvn clean compile

compile_run: compile run

clean:
	mvn clean
	rm -rf target
	rm -rf jar
	rm -rf javadoc

javadoc:
	mkdir -p ./javadoc
	mvn clean javadoc:javadoc
	if [ -d target/site/apidocs ]; then cp -R target/site/apidocs/. javadoc/; fi

build-jar:
	mvn clean package -DskipTests
	mkdir -p jar
	jar_file=$$(ls -1t target/plant-shop-jpa-spring-boot-hibernate-security-*.jar 2>/dev/null | head -n 1); \
	if [ -z "$$jar_file" ]; then echo "Aucun JAR généré."; exit 1; fi; \
	cp "$$jar_file" ./jar/plant-shop.jar

# ───────────────────────────────
#   Gestion de la seed
# ───────────────────────────────

seed:
	mvn clean compile exec:java \
		-Dexec.mainClass=com.planteshop.seed.SeedRunner \
		-Dspring.profiles.active=seed

compile-seed:
	mvn clean compile -Dspring.profiles.active=seed

seed-dev:
	if [ -d target ]; then rm -f $$(find target -name '*.class' -type f); fi
	$(MAKE) compile-seed
	$(MAKE) seed

seed-build:
	mvn clean package -DskipTests -Dspring.profiles.active=seed

# ───────────────────────────────
#   Gestion du test end-to-end
# ───────────────────────────────

tests:
	mvn clean test

compile-test:
	mvn clean test -DskipTests

test-dev: compile-test tests

test-build:
	mvn clean verify

# ───────────────────────────────
#   Commandes Base de données
# ───────────────────────────────

# Extrait la valeur depuis application.yml si disponible
# DB_NAME := $(shell sed -n 's/.*jdbc:postgresql:\/\/[^\/]*\/\([^?[:space:]]*\).*/\1/p' src/main/resources/application.yml | head -n 1 | tr -d '[:space:]')
DB_NAME = plant_shop_jpa

db-create:
	sudo -u postgres psql -c "CREATE DATABASE $(DB_NAME)" || echo "↳ base déjà existante."

db-migrate:
	sudo -u postgres psql -d $(DB_NAME) -f db/schema.sql

db-drop:
	sudo -u postgres psql -c "DROP DATABASE IF EXISTS $(DB_NAME);"

db-reset: db-drop db-create db-migrate

# ───────────────────────────────
#   Utilitaires
# ───────────────────────────────

tree:
	tree . -I "target|jar|javadoc"
