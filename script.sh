#!/usr/bin/env bash

echo "Mise à jour de application.properties pour PostgreSQL ..."
cat > src/main/resources/application.properties << "EOF"
spring.datasource.url=jdbc:postgresql://localhost:5432/plant_shop
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.web.resources.static-locations=classpath:/static/
EOF

echo "Suppression des fichiers liés à SQLite ..."
rm -f src/main/java/com/planteshop/config/SQLiteDialect.java
rm -f src/main/java/com/planteshop/config/SQLiteIdentityColumnSupport.java

echo "Nettoyage du dossier target (si présent) ..."
rm -rf target

echo "Ajout de la dépendance PostgreSQL dans pom.xml (si manquante) ..."
grep -q "postgresql" pom.xml || sed -i '/<dependencies>/a\
        <dependency>\n\
            <groupId>org.postgresql</groupId>\n\
            <artifactId>postgresql</artifactId>\n\
            <version>42.7.3</version>\n\
        </dependency>' pom.xml

echo "Migration terminée. Vous pouvez créer la base 'plant_shop' dans PostgreSQL si ce n'est pas déjà fait."
echo "Exécutez ensuite : mvn clean install"
