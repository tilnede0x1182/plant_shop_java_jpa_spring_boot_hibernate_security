package com.planteshop.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity;
	@Column(nullable = false)
	private double unitPrice;

	@ManyToOne
	@JoinColumn(name = "plant_id", foreignKey = @ForeignKey(name = "fk_orderitem_plant", foreignKeyDefinition = "FOREIGN KEY (plant_id) REFERENCES plant(id) ON DELETE CASCADE"))
	private Plant plant;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private CustomerOrder order;

	/** @return Long l'identifiant unique de l'item */
	public Long getId() {
		return id;
	}
	/** @param id Long l'identifiant à définir */
	public void setId(Long id) {
		this.id = id;
	}
	/** @return Integer la quantité commandée */
	public Integer getQuantity() {
		return quantity;
	}
	/** @param quantity Integer la quantité à définir */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	/** @return Plant la plante commandée */
	public Plant getPlant() {
		return plant;
	}
	/** @param plant Plant la plante à définir */
	public void setPlant(Plant plant) {
		this.plant = plant;
	}
	/** @return CustomerOrder la commande parente */
	public CustomerOrder getOrder() {
		return order;
	}
	/** @param order CustomerOrder la commande à définir */
	public void setOrder(CustomerOrder order) {
		this.order = order;
	}
	/** @return double le prix unitaire au moment de la commande */
	public double getUnitPrice() {
		return unitPrice;
	}
	/** @param unitPrice double le prix unitaire à définir */
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
}
