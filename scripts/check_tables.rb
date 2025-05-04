#!/usr/bin/env ruby

require "pg"

conn = PG.connect(
  dbname: "plant_shop",
  user: "tilnede0x1182",
  password: "tilnede0x1182",
  host: "localhost",
  port: 5432
)

res = conn.exec("SELECT tablename FROM pg_tables WHERE schemaname='public'")
tables = res.map { |r| r["tablename"] }

puts "\nTables présentes :"
puts "\n#{tables.sort}\n\n"

# Correspondance avec les entités JPA réelles
table_map = {
  "Plant" => "plant",
  "User" => "users",               # Attention : table explicitement nommée "users"
  "CustomerOrder" => "orders"      # Table nommée "orders"
}

table_map.each do |entity, table|
  if tables.include?(table)
    puts "Table pour #{entity} (#{table}) - présente"
  else
    puts "Table pour #{entity} (#{table}) - ABSENTE"
  end
end
puts ""
