#!/bin/bash

# Script de correction pour le projet PlantShop
# Usage: ./fix_project.sh

echo "🔧 Début des corrections..."

# 1. Supprimer la dépendance SQLite du pom.xml
echo "🗑️ Suppression de la dépendance SQLite..."
sed -i '/<dependency>/,/<\/dependency>/ {/sqlite-jdbc/d}' pom.xml
sed -i '/<!-- SQLite -->/d' pom.xml  # Supprime aussi les commentaires associés si existants

# 2. Mettre à jour la SecurityConfig
SECURITY_CONFIG_FILE="src/main/java/com/planteshop/config/SecurityConfig.java"
echo "🔐 Modification du fichier de sécurité: $SECURITY_CONFIG_FILE"

# Crée une copie de sauvegarde
cp "$SECURITY_CONFIG_FILE" "$SECURITY_CONFIG_FILE.bak"

# Modifie le fichier avec awk
awk '
/\.requestMatchers\("\/api\/auth\/\*\*"\)\.permitAll\(\)/ {
    print $0
    print "            .requestMatchers(\"/api/plants/**\").permitAll() // Ajouté par le script de correction"
    next
}
{ print }
' "$SECURITY_CONFIG_FILE.bak" > "$SECURITY_CONFIG_FILE"

# 3. Ajouter les logs SQL dans application.properties
APP_PROPERTIES="src/main/resources/application.properties"
echo "📝 Ajout des logs SQL dans $APP_PROPERTIES"

cat <<EOT >> "$APP_PROPERTIES"

# Logging SQL ajouté par le script de correction
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
EOT

# 4. Vérifier/Créer la base de données (nécessite psql)
echo "🛢️ Vérification de la base de données PostgreSQL..."
sudo -u postgres psql <<PGSCRIPT
CREATE DATABASE plant_shop;
CREATE USER tilnede0x1182 WITH PASSWORD 'tilnede0x1182';
GRANT ALL PRIVILEGES ON DATABASE plant_shop TO tilnede0x1182;
\q
PGSCRIPT

# 5. Donner les droits d'exécution au script
chmod +x "$0"

echo "✅ Corrections terminées !"
echo "➡️ Étapes manuelles restantes:"
echo "1. Redémarrer l'application Spring Boot"
echo "2. Vérifier les logs au démarrage"
echo "3. Tester avec curl: curl http://localhost:8080/api/plants"
