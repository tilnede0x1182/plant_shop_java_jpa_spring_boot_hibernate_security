package com.planteshop.model.entity;

import jakarta.persistence.*;

@Entity
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer stock;

    /** @return Long l'identifiant unique de la plante */
    public Long getId() { return id; }
    /** @param id Long l'identifiant à définir */
    public void setId(Long id) { this.id = id; }
    /** @return String le nom de la plante */
    public String getName() { return name; }
    /** @param name String le nom à définir */
    public void setName(String name) { this.name = name; }
    /** @return String la description de la plante */
    public String getDescription() { return description; }
    /** @param description String la description à définir */
    public void setDescription(String description) { this.description = description; }
    /** @return Double le prix de la plante */
    public Double getPrice() { return price; }
    /** @param price Double le prix à définir */
    public void setPrice(Double price) { this.price = price; }
    /** @return String la catégorie de la plante */
    public String getCategory() { return category; }
    /** @param category String la catégorie à définir */
    public void setCategory(String category) { this.category = category; }
    /** @return Integer le stock disponible */
    public Integer getStock() { return stock; }
    /** @param stock Integer le stock à définir */
    public void setStock(Integer stock) { this.stock = stock; }
}
