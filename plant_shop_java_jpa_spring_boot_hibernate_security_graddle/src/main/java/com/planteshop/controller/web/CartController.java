package com.planteshop.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    /**
     * Affiche la page du panier.
     *
     * @return String le nom de la vue du panier
     */
    @GetMapping("/cart")
    public String cart() {
        return "cart/cart";
    }
}
