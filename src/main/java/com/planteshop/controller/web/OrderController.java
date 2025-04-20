package com.planteshop.controller.web;

import com.planteshop.model.entity.CustomerOrder;
import com.planteshop.model.entity.User;
import com.planteshop.repository.OrderRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderController {

	private final OrderRepository orderRepository;

	public OrderController(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@GetMapping("/orders")
	public String listOrders(@AuthenticationPrincipal User user, Model model) {
		List<CustomerOrder> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
		model.addAttribute("orders", orders);
		return "orders/index";
	}
}
