class CartManager {
	static load() {
		return JSON.parse(localStorage.getItem("cart") || "[]");
	}

	static save(cart) {
		localStorage.setItem("cart", JSON.stringify(cart));
	}

	static countItems() {
		return this.load().reduce((sum, item) => sum + item.qty, 0);
	}

	static refreshUI() {
		const count = this.countItems();
		document
			.querySelectorAll(".cart-count")
			.forEach((el) => (el.textContent = count));
		const link = document.getElementById("cart-link");
		if (link) link.textContent = `Panier (${count})`;
	}
}

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
