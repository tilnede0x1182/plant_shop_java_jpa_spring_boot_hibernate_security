class PlantCatalog {
  static init() {
    document.querySelectorAll('.add-to-cart')
      .forEach(btn => btn.addEventListener('click', e => this.addToCart(e)));
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
