class CartPage {
  static init() {
    this.render();
    document.getElementById('clear-cart')
      ?.addEventListener('click', () => this.clear());
  }

  static render() {
    const cart = CartManager.load();
    const tbody = document.querySelector('#cart-items tbody');
    tbody.innerHTML = '';

    cart.forEach(item => {
      const row = document.createElement('tr');
      row.innerHTML = `
        <td>${item.name}</td>
        <td>${item.qty}</td>
        <td>${(item.price * item.qty).toFixed(2)} €</td>
      `;
      tbody.appendChild(row);
    });

    const total = cart.reduce((sum, item) => sum + item.price * item.qty, 0);
    document.getElementById('cart-total').textContent = total.toFixed(2) + ' €';
  }

  static clear() {
    CartManager.save([]);
    this.render();
    CartManager.refreshUI();
  }
}

document.addEventListener('DOMContentLoaded', () => {
  CartPage.init();
});
