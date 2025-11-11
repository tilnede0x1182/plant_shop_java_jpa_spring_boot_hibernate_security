// # Classe CartPage

/**
 * Gère l'affichage, la mise à jour, et la validation du panier côté page.
 */
class CartPage {
	/**
	 * Initialise les événements DOM, le rendu initial et les boutons.
	 */
	static init() {
		this.cartTableBody = document.getElementById("cartBody");
		this.totalElement = document.getElementById("cartTotal");
		this.messageElement = document.getElementById("checkoutMessage");

		const form = document.getElementById("checkoutForm");
		const clearButton = document.getElementById("clearCart");

		form?.addEventListener("submit", (submitEvent) => this.handleCheckout(submitEvent));
		clearButton?.addEventListener("click", () => this.clearCart());
		this.cartTableBody.addEventListener("click", (mouseEvent) => this.handleRemove(mouseEvent));

		this.render();
	}

	/**
	 * Affiche dynamiquement les articles et le total du panier.
	 */
	static render() {
		const currentCart = CartManager.load();
		this.cartTableBody.innerHTML = "";
		let cartTotal = 0;

		if (currentCart.length === 0) {
			this.#renderEmptyCartMessage();
		} else {
			currentCart.forEach((product, position) => {
				const rowElement = this.#buildCartRow(product, position);
				this.cartTableBody.appendChild(rowElement);
				cartTotal += product.qty * product.price;
			});
		}
		this.totalElement.textContent = cartTotal.toFixed(2);
	}

	/**
	 * Génère un élément de ligne HTML pour un article du panier.
	 * @param {Object} product Produit à afficher
	 * @param {number} position Index dans le tableau
	 * @returns {HTMLTableRowElement}
	 */
	static #buildCartRow(product, position) {
		const row = document.createElement("tr");
		row.appendChild(this.#buildNameCell(product));
		row.appendChild(this.#buildPriceCell(product));
		row.appendChild(this.#buildQuantityCell(product));
		row.appendChild(this.#buildSubtotalCell(product));
		row.appendChild(this.#buildRemoveCell(position));
		return row;
	}

	/**
	 * Crée une cellule avec lien vers la fiche produit.
	 */
	static #buildNameCell(product) {
		const cell = document.createElement("td");
		const link = document.createElement("a");
		link.href = `/plants/${product.id}`;
		link.className = "product-link";
		link.textContent = product.name;
		cell.appendChild(link);
		return cell;
	}

	/**
	 * Crée une cellule pour le prix unitaire.
	 */
	static #buildPriceCell(product) {
		const cell = document.createElement("td");
		cell.textContent = product.price.toFixed(2);
		return cell;
	}

	/**
	 * Crée une cellule avec champ quantité modifiable.
	 */
	static #buildQuantityCell(product) {
		const cell = document.createElement("td");
		const inputField = document.createElement("input");
		inputField.type = "number";
		inputField.min = "1";
		inputField.value = product.qty;
		inputField.className = "form-control form-control-sm qty-input";
		inputField.style.width = "80px";
		inputField.dataset.cartId = product.id;
		inputField.dataset.stock = product.stock;
		inputField.onkeydown = (keyboardEvent) =>
			keyboardEvent.key !== "e" && keyboardEvent.key !== "-";
		this.#attachQuantityHandler(inputField);
		cell.appendChild(inputField);
		return cell;
	}

	/**
	 * Crée une cellule affichant le sous-total.
	 */
	static #buildSubtotalCell(product) {
		const cell = document.createElement("td");
		const subtotal = product.qty * product.price;
		cell.textContent = subtotal.toFixed(2);
		return cell;
	}

	/**
	 * Crée une cellule avec bouton de suppression.
	 */
	static #buildRemoveCell(position) {
		const cell = document.createElement("td");
		const button = document.createElement("button");
		button.className = "btn btn-sm btn-danger";
		button.textContent = "✕";
		button.dataset.i = position;
		cell.appendChild(button);
		return cell;
	}

	/**
	 * Ajoute les gestionnaires input et blur au champ quantité.
	 * @param {HTMLInputElement} inputField
	 */
	static #attachQuantityHandler(inputField) {
		let delayTimer;
		const handler = () => {
			clearTimeout(delayTimer);
			delayTimer = setTimeout(() => {
				const currentCart = CartManager.load();
				const productId = inputField.dataset.cartId;
				const stockMax = parseInt(inputField.dataset.stock) || 1;
				let quantity = parseInt(inputField.value);
				if (isNaN(quantity) || quantity < 1) quantity = 1;
				if (quantity > stockMax) quantity = stockMax;
				inputField.value = quantity;

				const product = currentCart.find((p) => p.id == productId);
				if (product) product.qty = quantity;

				CartManager.save(currentCart);
				CartPage.render();
				refreshCart();
			}, 300);
		};
		inputField.addEventListener("input", handler);
		inputField.addEventListener("blur", handler);
	}

	/**
	 * Supprime un article cliqué du panier.
	 * @param {MouseEvent} mouseEvent
	 */
	static handleRemove(mouseEvent) {
		if (mouseEvent.target.matches("button[data-i]")) {
			const currentCart = CartManager.load();
			currentCart.splice(mouseEvent.target.dataset.i, 1);
			CartManager.save(currentCart);
			this.render();
			refreshCart();
		}
	}

	/**
	 * Gère la soumission du panier vers le serveur.
	 * @param {SubmitEvent} submitEvent
	 */
	static handleCheckout(submitEvent) {
		submitEvent.preventDefault();
		if (window.userRole === "visitor") {
			window.location.href = "/login";
			return;
		}
		const currentCart = CartManager.load();
		if (currentCart.length === 0) {
			this.showMsg("Votre panier est vide.", "danger");
			return;
		}
		this.#sendCheckoutRequest(currentCart);
	}

	/**
	 * Envoie la requête POST de checkout au serveur.
	 * @param {Object[]} currentCart
	 */
	static #sendCheckoutRequest(currentCart) {
		fetch("/orders/checkout", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(currentCart),
		})
			.then((response) => response.text().then((text) => ({ ok: response.ok, text })))
			.then(({ ok, text }) => {
				if (ok && text === "OK") {
					localStorage.removeItem("cart");
					refreshCart();
					this.showMsg("Commande validée ! Redirection…", "success");
					setTimeout(() => (window.location.href = "/orders"), 0);
				} else {
					this.showMsg("Erreur lors de la commande : " + text, "danger");
				}
			})
			.catch(() => {
				this.showMsg("Erreur réseau.", "danger");
			});
	}

	/**
	 * Vide complètement le panier et recharge l'interface.
	 */
	static clearCart() {
		localStorage.removeItem("cart");
		this.render();
		refreshCart();
	}

	/**
	 * Affiche un message utilisateur type Bootstrap.
	 * @param {string} message
	 * @param {string} type
	 */
	static showMsg(message, type) {
		this.messageElement.className = `alert alert-${type}`;
		this.messageElement.textContent = message;
	}

	/**
	 * Affiche un message lorsque le panier est vide.
	 */
	static #renderEmptyCartMessage() {
		const emptyRow = document.createElement("tr");
		const emptyCell = document.createElement("td");
		emptyCell.colSpan = 5;
		emptyCell.className = "text-center";
		emptyCell.textContent = "Votre panier est vide.";
		emptyRow.appendChild(emptyCell);
		this.cartTableBody.appendChild(emptyRow);
	}
}

// # Lancement

/**
 * Initialise le rendu du panier au chargement.
 */
document.addEventListener("DOMContentLoaded", () => {
	CartPage.init();
});
