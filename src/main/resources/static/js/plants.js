class PlantCatalog {
  static init() {
    document.querySelectorAll('.add-to-cart').forEach(btn => {
      btn.addEventListener('click', e => {
        e.stopPropagation();   // bloque la remontée vers l’élément <a>
        e.preventDefault();    // annule la navigation

        const id = btn.dataset.id;
        const name = btn.dataset.name;
        const price = parseFloat(btn.dataset.price);

        const cart = JSON.parse(localStorage.getItem('cart') || '[]');
        const item = cart.find(i => i.id == id);
        item ? item.qty++ : cart.push({id, name, price, qty: 1});
        localStorage.setItem('cart', JSON.stringify(cart));
        refreshUI();
      });
    });
  }

  static addToCart(event) {
    const btn = event.currentTarget;
    const plant = {
      id: btn.dataset.id,
      name: btn.dataset.name,
      price: parseFloat(btn.dataset.price),
      qty: 1
    };

    let cart = CartManager.load();
    const existing = cart.find(item => item.id === plant.id);

    if (existing) {
      existing.qty += 1;
    } else {
      cart.push(plant);
    }

    CartManager.save(cart);
    CartManager.refreshUI();
  }
}

document.addEventListener('DOMContentLoaded', () => {
  PlantCatalog.init();
});
