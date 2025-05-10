class CartPage {
	static init() {
		this.body = document.getElementById("cartBody");
		this.totalE = document.getElementById("cartTotal");
		this.msgEl = document.getElementById("checkoutMessage");

		document.getElementById("checkoutForm")?.addEventListener("submit", (e) => this.handleCheckout(e));
		document.getElementById("clearCart")?.addEventListener("click", () => this.clearCart());
		this.body.addEventListener("click", (e) => this.handleRemove(e));

		this.render();
	}

	static render() {
		const cart = CartManager.load();
		this.body.innerHTML = "";
		let total = 0;

		if (cart.length === 0) {
			const row = document.createElement("tr");
			const cell = document.createElement("td");
			cell.colSpan = 5;
			cell.className = "text-center";
			cell.textContent = "Votre panier est vide.";
			row.appendChild(cell);
			this.body.appendChild(row);
		} else {
			cart.forEach((item, i) => {
				const row = document.createElement("tr");

				const colName = document.createElement("td");
				const link = document.createElement("a");
				link.href = `/plants/${item.id}`;
				link.className = "product-link";
				link.textContent = item.name;
				colName.appendChild(link);

				const colPrice = document.createElement("td");
				colPrice.textContent = item.price.toFixed(2);

				const colQty = document.createElement("td");
				const input = document.createElement("input");
				input.type = "number";
				input.min = "1";
				input.value = item.qty;
				input.className = "form-control form-control-sm qty-input";
				input.style.width = "80px";
				input.dataset.i = i;
				input.onkeydown = (e) => e.key !== "e" && e.key !== "-";
				this.attachQtyListener(input);
				colQty.appendChild(input);

				const colSub = document.createElement("td");
				colSub.textContent = (item.qty * item.price).toFixed(2);

				const colDel = document.createElement("td");
				const btn = document.createElement("button");
				btn.className = "btn btn-sm btn-danger";
				btn.textContent = "✕";
				btn.dataset.i = i;
				colDel.appendChild(btn);

				[colName, colPrice, colQty, colSub, colDel].forEach(el => row.appendChild(el));
				this.body.appendChild(row);

				total += item.qty * item.price;
			});
		}

		this.totalE.textContent = total.toFixed(2);
	}

	static attachQtyListener(input) {
		let timer;
		const handler = (e) => {
			clearTimeout(timer);
			timer = setTimeout(() => {
				const i = e.target.dataset.i;
				let val = parseInt(e.target.value);
				if (isNaN(val) || val < 1) val = 1;
				e.target.value = val;
				const cart = CartManager.load();
				cart[i].qty = val;
				CartManager.save(cart);
				this.render();
				refreshCart();
			}, 300);
		};
		input.addEventListener("input", handler);
		input.addEventListener("blur", handler);
	}

	static handleRemove(e) {
		if (e.target.matches("button[data-i]")) {
			const cart = CartManager.load();
			cart.splice(e.target.dataset.i, 1);
			CartManager.save(cart);
			this.render();
			refreshCart();
		}
	}

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
					this.showMsg("Erreur lors de la commande : " + text, "danger");
				}
			})
			.catch(() => {
				this.showMsg("Erreur réseau.", "danger");
			});
	}

	static clearCart() {
		localStorage.removeItem("cart");
		this.render();
		refreshCart();
	}

	static showMsg(text, type) {
		this.msgEl.className = `alert alert-${type}`;
		this.msgEl.textContent = text;
	}
}

document.addEventListener("DOMContentLoaded", () => {
	CartPage.init();
});
