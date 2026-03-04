package com.planteshop.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class CustomerOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalPrice;
    private String status;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

		@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();

    /** @return Long l'identifiant unique de la commande */
    public Long getId() { return id; }
    /** @param id Long l'identifiant à définir */
    public void setId(Long id) { this.id = id; }
    /** @return Double le prix total de la commande */
    public Double getTotalPrice() { return totalPrice; }
    /** @param totalPrice Double le prix total à définir */
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
    /** @return String le statut de la commande */
    public String getStatus() { return status; }
    /** @param status String le statut à définir */
    public void setStatus(String status) { this.status = status; }
    /** @return LocalDateTime la date de création */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /** @param createdAt LocalDateTime la date de création à définir */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /** @return User l'utilisateur propriétaire de la commande */
    public User getUser() { return user; }
    /** @param user User l'utilisateur à définir */
    public void setUser(User user) { this.user = user; }
    /** @return List les items de la commande */
    public List<OrderItem> getItems() { return items; }
    /** @param items List les items à définir */
    public void setItems(List<OrderItem> items) { this.items = items; }
}
