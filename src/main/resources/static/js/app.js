/**
 * Gère le panier via le localStorage
 * @class CartManager
 */
class CartManager {
	/**
	 * Charge le panier depuis le localStorage
	 * @returns {Array<Object>} liste des articles dans le panier
	 */
	static load() {
		return JSON.parse(localStorage.getItem("cart") || "[]");
	}

	/**
	 * Ajoute une plante au panier, avec gestion du stock initial.
	 * @param {string|number} id identifiant unique de la plante
	 * @param {string} name nom de la plante
	 * @param {number} price prix unitaire
	 * @param {number} stock quantité disponible
	 */
	static add(id, name, price, stock) {
    // console.log("[CartManager.add] received stock =", stock);
		const cart = this.load();
		const existing = cart.find((item) => item.id == id);
		if (existing) {
      // console.log("[CartManager.add] item exists, current qty =", existing.qty, "stock (before) =", existing.stock);
			existing.qty += 1;
      existing.stock = stock;
      // console.log("[CartManager.add] stock updated to =", existing.stock);
		} else {
      // console.log("[CartManager.add] new item added with stock =", stock);
			cart.push({ id, name, price, qty: 1, stock });
		}
		this.save(cart);
		this.refreshUI();
	}

	/**
	 * Sauvegarde le panier dans le localStorage
	 * @param {Array<Object>} cart liste des articles à sauvegarder
	 */
	static save(cart) {
		localStorage.setItem("cart", JSON.stringify(cart));
	}

	/**
	 * Calcule le nombre total d'articles dans le panier
	 * @returns {number} total des quantités
	 */
	static countItems() {
		return this.load().reduce((sum, item) => sum + item.qty, 0);
	}

	/**
	 * Met à jour les éléments de l’interface liés au panier
	 */
	static refreshUI() {
		const count = this.countItems();
		document
			.querySelectorAll(".cart-count")
			.forEach((el) => (el.textContent = count));
		const link = document.getElementById("cart-link");
		if (link) link.textContent = `Panier (${count})`;
	}
}

/**
 * Initialise les tooltips Bootstrap et l’UI du panier à chargement de page
 */
document.addEventListener("DOMContentLoaded", () => {
	[...document.querySelectorAll('[data-bs-toggle="tooltip"]')].forEach(
		(el) => new bootstrap.Tooltip(el)
	);
	CartManager.refreshUI();
	window.addEventListener(
		"storage",
		(e) => e.key === "cart" && CartManager.refreshUI()
	);
});

/**
 * @function window.refreshCart
 * @description Alias global pour rafraîchir l’UI du panier
 */
window.refreshCart = () => CartManager.refreshUI();

/**
 * @function window.refreshUI
 * @description Autre alias global pour rafraîchir l’UI du panier
 */
window.refreshUI = () => CartManager.refreshUI();
