/**
 * Gère l'affichage, la mise à jour, et la validation du panier côté page.
 * @class CartPage
 */
class CartPage {
	/**
	 * Initialise les éléments du DOM, les événements et le rendu initial.
	 */
	static init() {
		/** @member {HTMLElement} this.body - Corps du tableau du panier */
		this.body = document.getElementById("cartBody");

		/** @member {HTMLElement} this.totalE - Élément affichant le total */
		this.totalE = document.getElementById("cartTotal");

		/** @member {HTMLElement} this.msgEl - Élément pour les messages */
		this.msgEl = document.getElementById("checkoutMessage");

		// Événement de soumission du formulaire
		document
			.getElementById("checkoutForm")
			?.addEventListener("submit", (e) => this.handleCheckout(e));

		// Bouton pour vider le panier
		document
			.getElementById("clearCart")
			?.addEventListener("click", () => this.clearCart());

		// Événement pour suppression d'article
		this.body.addEventListener("click", (e) => this.handleRemove(e));

		this.render();
	}

	/**
	 * Affiche dynamiquement le contenu du panier et le total.
	 */
	static render() {
		const cart = CartManager.load();
		this.body.innerHTML = "";
		let total = 0;

		if (cart.length === 0) {
			// Affiche un message si le panier est vide
			const row = document.createElement("tr");
			const cell = document.createElement("td");
			cell.colSpan = 5;
			cell.className = "text-center";
			cell.textContent = "Votre panier est vide.";
			row.appendChild(cell);
			this.body.appendChild(row);
		} else {
			// Affiche chaque article dans le panier
			cart.forEach((item, i) => {
				const row = document.createElement("tr");

				// Nom + lien
				const colName = document.createElement("td");
				const link = document.createElement("a");
				link.href = `/plants/${item.id}`;
				link.className = "product-link";
				link.textContent = item.name;
				colName.appendChild(link);

				// Prix unitaire
				const colPrice = document.createElement("td");
				colPrice.textContent = item.price.toFixed(2);

				// Quantité modifiable
				const colQty = document.createElement("td");
				const input = document.createElement("input");
				input.type = "number";
				input.min = "1";
				input.value = item.qty;
				input.className = "form-control form-control-sm qty-input";
				input.style.width = "80px";
				input.dataset.cartId = item.id;
				input.dataset.stock = item.stock;
				input.onkeydown = (e) => e.key !== "e" && e.key !== "-";
				this.attachQtyListener(input);
				colQty.appendChild(input);

				// Sous-total
				const colSub = document.createElement("td");
				colSub.textContent = (item.qty * item.price).toFixed(2);

				// Bouton supprimer
				const colDel = document.createElement("td");
				const btn = document.createElement("button");
				btn.className = "btn btn-sm btn-danger";
				btn.textContent = "✕";
				btn.dataset.i = i;
				colDel.appendChild(btn);

				[colName, colPrice, colQty, colSub, colDel].forEach((el) =>
					row.appendChild(el)
				);
				this.body.appendChild(row);

				total += item.qty * item.price;
			});
		}

		this.totalE.textContent = total.toFixed(2);
	}

	/**
	 * Attache les événements à un champ de quantité pour mettre à jour l’article.
	 * @param {HTMLInputElement} input champ de saisie de la quantité
	 */
	static attachQtyListener(input) {
		let timer;
		const handler = () => {
			clearTimeout(timer);
			timer = setTimeout(() => {
				const cart = CartManager.load();
				const id = input.dataset.cartId;
        // console.log("[cart.js] input.dataset.stock =", input.dataset.stock);
				const stock = parseInt(input.dataset.stock) || 1;
        // console.log("[cart.js] parsed stock =", stock);
				let val = parseInt(input.value);
				// if (isNaN(val)) console.log("val is NaN, val = " + val);
				if (isNaN(val) || val < 1) val = 1;
				// console.log("Val = " + val);
				if (val > stock) val = stock;
				input.value = val;
				const item = cart.find((p) => p.id == id);
				if (item) item.qty = val;
				CartManager.save(cart);
				CartPage.render();
				refreshCart();
			}, 300);
		};
		input.addEventListener("input", handler);
		input.addEventListener("blur", handler);
	}

	/**
	 * Gère la suppression d’un article via le bouton.
	 * @param {MouseEvent} e événement de clic
	 */
	static handleRemove(e) {
		if (e.target.matches("button[data-i]")) {
			const cart = CartManager.load();
			cart.splice(e.target.dataset.i, 1);
			CartManager.save(cart);
			this.render();
			refreshCart();
		}
	}

	/**
	 * Gère la soumission du panier (checkout).
	 * @param {SubmitEvent} e événement de soumission
	 */
	static handleCheckout(e) {
		e.preventDefault();

		if (window.userRole === "visitor") {
			window.location.href = "/login";
			return;
		}

		const cart = CartManager.load();
		if (cart.length === 0) {
			this.showMsg("Votre panier est vide.", "danger");
			return;
		}

		fetch("/orders/checkout", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(cart),
		})
			.then((res) => res.text().then((text) => ({ ok: res.ok, text })))
			.then(({ ok, text }) => {
				if (ok && text === "OK") {
					localStorage.removeItem("cart");
					refreshCart();
					this.showMsg("Commande validée ! Redirection…", "success");
					setTimeout(() => (window.location.href = "/orders"), 0);
				} else {
					this.showMsg(
						"Erreur lors de la commande : " + text,
						"danger"
					);
				}
			})
			.catch(() => {
				this.showMsg("Erreur réseau.", "danger");
			});
	}

	/**
	 * Vide le panier et met à jour l'interface.
	 */
	static clearCart() {
		localStorage.removeItem("cart");
		this.render();
		refreshCart();
	}

	/**
	 * Affiche un message utilisateur (erreur, succès…).
	 * @param {string} text contenu du message
	 * @param {string} type type Bootstrap (`success`, `danger`, etc.)
	 */
	static showMsg(text, type) {
		this.msgEl.className = `alert alert-${type}`;
		this.msgEl.textContent = text;
	}
}

/**
 * Initialise la page du panier à chargement.
 */
document.addEventListener("DOMContentLoaded", () => {
	CartPage.init();
});
