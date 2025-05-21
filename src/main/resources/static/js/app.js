// # Classe CartManager

/**
 * Gère le panier via le localStorage
 */
class CartManager {
	/**
	 * Charge le panier depuis le localStorage
	 * @returns {Array<Object>} Liste des articles dans le panier
	 */
	static load() {
		const cartJson = localStorage.getItem("cart") || "[]";
		return JSON.parse(cartJson);
	}

	/**
	 * Sauvegarde le panier dans le localStorage
	 * @param {Array<Object>} cartList Liste des articles à sauvegarder
	 */
	static save(cartList) {
		localStorage.setItem("cart", JSON.stringify(cartList));
	}

	/**
	 * Applique les changements du panier et met à jour l'interface.
	 * @param {Array<Object>} cartList État du panier à valider
	 */
	static #commitCart(cartList) {
		this.save(cartList);
		this.refreshUI();
	}

	/**
	 * Ajoute un article au panier, ou incrémente sa quantité.
	 * Gère aussi le dépassement de stock.
	 * @param {number} productId Identifiant du produit
	 * @param {string} productName Nom du produit
	 * @param {number} unitPrice Prix unitaire
	 * @param {number} stockMax Quantité maximale disponible
	 */
	static add(productId, productName, unitPrice, stockMax) {
		const cartList = this.load();
		const product = cartList.find((entry) => entry && entry.id == productId);

		if (!product) {
			cartList.push({ id: productId, name: productName, price: unitPrice, qty: 1, stock: stockMax });
			this.#commitCart(cartList);
			return;
		}

		if (product.qty >= stockMax) {
			showStockAlert(productName, stockMax);
			setTimeout(() => {
				product.qty = stockMax;
				this.#commitCart(cartList);
			}, 300);
		} else {
			product.qty++;
			this.#commitCart(cartList);
		}
	}

	/**
	 * Calcule le nombre total d'articles dans le panier
	 * @returns {number} Total des quantités
	 */
	static countItems() {
		const cartList = this.load();
		return cartList.filter((entry) => entry && typeof entry.qty === "number").reduce((total, entry) => total + entry.qty, 0);
	}

	/**
	 * Met à jour les éléments de l’interface affichant le compteur
	 */
	static refreshUI() {
		const itemCount = this.countItems();

		document.querySelectorAll(".cart-count").forEach((element) => {
			element.textContent = itemCount;
		});

		const navbarLink = document.getElementById("cart-link");
		if (navbarLink) {
			navbarLink.innerHTML = itemCount > 0 ? `Mon Panier (${itemCount})` : `Mon Panier`;
		}
	}
}

// # Fonctions utilitaires

/**
 * Affiche une alerte pour informer d'un stock insuffisant
 * @param {string} plantName Nom du produit
 * @param {number} stockLeft Quantité restante
 */
function showStockAlert(plantName, stockLeft) {
	const alertElement = document.createElement("div");
	alertElement.className = "alert alert-warning fade position-absolute top-0 start-50 translate-middle-x mt-3 shadow";
	alertElement.role = "alert";
	alertElement.style.zIndex = "1055";
	alertElement.style.maxWidth = "600px";
	alertElement.style.pointerEvents = "none";

	const textStart = document.createTextNode("Stock insuffisant pour pour cette plante (");
	const strongText = document.createElement("strong");
	strongText.textContent = plantName;
	const textEnd = document.createTextNode(`), actuellement, il en reste ${stockLeft}.`);

	alertElement.appendChild(textStart);
	alertElement.appendChild(strongText);
	alertElement.appendChild(textEnd);

	document.body.appendChild(alertElement);
	setTimeout(() => alertElement.classList.add("show"), 10);
	setTimeout(() => {
		alertElement.classList.remove("show");
		alertElement.classList.add("fade");
		setTimeout(() => alertElement.remove(), 300);
	}, 3000);
}

// # Initialisation DOM

/**
 * Initialise les tooltips Bootstrap et les compteurs panier
 */
document.addEventListener("DOMContentLoaded", () => {
	const tooltipElements = document.querySelectorAll('[data-bs-toggle="tooltip"]');
	tooltipElements.forEach((element) => new bootstrap.Tooltip(element));

	CartManager.refreshUI();

	window.addEventListener("storage", (storageEvent) => {
		if (storageEvent.key === "cart") {
			CartManager.refreshUI();
		}
	});
});

// # Fonctions globales

/**
 * Alias global pour rafraîchir l'interface panier
 */
window.refreshCart = () => CartManager.refreshUI();

/**
 * Alias secondaire pour compatibilité
 */
window.refreshUI = () => CartManager.refreshUI();
