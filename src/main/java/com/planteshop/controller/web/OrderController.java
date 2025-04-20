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

	public OrderController(OrderRepository orderRepository, PlantRepository plantRepository) {
		this.orderRepository = orderRepository;
		this.plantRepository = plantRepository;
	}

	@GetMapping("/orders")
	public String listOrders(@AuthenticationPrincipal User user, Model model) {
		List<CustomerOrder> orders = orderRepository.findByUserOrderByIdDesc(user);
		model.addAttribute("orders", orders);
		return "orders/index";
	}

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

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public int getQty() {
			return qty;
		}

		public void setQty(int qty) {
			this.qty = qty;
		}
	}
}
