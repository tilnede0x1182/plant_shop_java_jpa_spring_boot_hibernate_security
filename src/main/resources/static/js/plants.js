/**
 * Catalogue des plantes affichées, gestion de l’ajout au panier.
 * @class PlantCatalog
 */
class PlantCatalog {
	/**
	 * Initialise les boutons d’ajout au panier sur chaque fiche plante.
	 * Bloque la navigation via <a>, puis ajoute la plante au panier.
	 */
	static init() {
		document.querySelectorAll(".add-to-cart").forEach((btn) => {
			btn.addEventListener("click", (exception) => {
				exception.stopPropagation(); // empêche la propagation vers le lien parent
				exception.preventDefault(); // empêche la navigation

				const id = btn.dataset.id;
				const name = btn.dataset.name;
				const price = parseFloat(btn.dataset.price);
				const stock = parseInt(btn.dataset.stock);

        // console.log("[plants.js] btn.dataset.stock =", btn.dataset.stock);
				CartManager.add(id, name, price, stock);
			});
		});
	}

	/**
	 * Ajoute une plante au panier via bouton personnalisé.
	 * @param {Event} event événement déclenché par le bouton
	 */
	static addToCart(event) {
		const btn = event.currentTarget;

		/** @type {{id: string, name: string, price: number, qty: number}} */
		const plant = {
			id: btn.dataset.id,
			name: btn.dataset.name,
			price: parseFloat(btn.dataset.price),
			qty: 1,
		};

		let cart = CartManager.load();
		const existing = cart.find((item) => item.id === plant.id);

		if (existing) {
			existing.qty += 1;
		} else {
			cart.push(plant);
		}

		CartManager.save(cart);
		CartManager.refreshUI();
	}
}

/**
 * Initialise le catalogue à chargement de la page.
 */
document.addEventListener("DOMContentLoaded", () => {
	PlantCatalog.init();
});
