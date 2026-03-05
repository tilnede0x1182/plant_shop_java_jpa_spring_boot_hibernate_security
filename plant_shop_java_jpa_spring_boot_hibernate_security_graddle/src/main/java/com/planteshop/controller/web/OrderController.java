package com.planteshop.controller.web;

import com.planteshop.model.entity.CustomerOrder;
import com.planteshop.model.entity.OrderItem;
import com.planteshop.model.entity.Plant;
import com.planteshop.model.entity.User;
import com.planteshop.repository.OrderRepository;
import com.planteshop.repository.PlantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class OrderController {

	private final OrderRepository orderRepository;
	private final PlantRepository plantRepository;

	/**
	 * Constructeur avec injection des repositories.
	 *
	 * @param orderRepository OrderRepository le repository des commandes
	 * @param plantRepository PlantRepository le repository des plantes
	 */
	public OrderController(OrderRepository orderRepository, PlantRepository plantRepository) {
		this.orderRepository = orderRepository;
		this.plantRepository = plantRepository;
	}

	/**
	 * Affiche la liste des commandes de l'utilisateur connecte.
	 *
	 * @param user User l'utilisateur authentifie
	 * @param model Model le modele pour la vue
	 * @return String le nom de la vue
	 */
	@GetMapping("/orders")
	public String listOrders(@AuthenticationPrincipal User user, Model model) {
		List<CustomerOrder> orders = orderRepository.findByUserOrderByIdDesc(user);

		model.addAttribute("orders", orders);
		return "orders/index";
	}

	/**
	 * Traite le passage de commande depuis le panier.
	 * Verifie le stock, decremente et cree la commande.
	 *
	 * @param user User l'utilisateur authentifie
	 * @param cartItems List<CartItemDto> les articles du panier
	 * @return ResponseEntity<String> OK ou message d'erreur
	 */
	@PostMapping("/orders/checkout")
	public ResponseEntity<String> checkout(@AuthenticationPrincipal User user,
	                       @RequestBody List<CartItemDto> cartItems) {
													if (user == null) {
														return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("NOT_AUTHENTICATED");
													}
		CustomerOrder order = new CustomerOrder();
		order.setUser(user);

		double total = 0;

		for (CartItemDto itemDto : cartItems) {

			Plant plant = plantRepository.findById(itemDto.getId()).orElse(null);
			if (plant == null || plant.getStock() < itemDto.getQty()) {
				return ResponseEntity.badRequest().body("Erreur : produit indisponible");
			}

			plant.setStock(plant.getStock() - itemDto.getQty());
			plantRepository.save(plant);

			OrderItem item = new OrderItem();
			item.setPlant(plant);
			item.setQuantity(itemDto.getQty());
			item.setUnitPrice(plant.getPrice());
			item.setOrder(order);

			order.getItems().add(item);
			total += plant.getPrice() * itemDto.getQty();
		}

		order.setTotalPrice(total);
		orderRepository.save(order);

		return ResponseEntity.ok("OK");

	}

	public static class CartItemDto {
		private Long id;
		private int qty;

		/**
		 * Retourne l'identifiant de la plante.
		 *
		 * @return Long l'identifiant
		 */
		public Long getId() {
			return id;
		}

		/**
		 * Definit l'identifiant de la plante.
		 *
		 * @param id Long l'identifiant
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * Retourne la quantite.
		 *
		 * @return int la quantite
		 */
		public int getQty() {
			return qty;
		}

		/**
		 * Definit la quantite.
		 *
		 * @param qty int la quantite
		 */
		public void setQty(int qty) {
			this.qty = qty;
		}
	}
}
