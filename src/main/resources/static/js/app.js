// Gestion du panier
document.addEventListener('DOMContentLoaded', function() {
    // Initialisation des tooltips Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    });

    // Gestion du compteur du panier
    updateCartCount();
});

function updateCartCount() {
    const cart = JSON.parse(localStorage.getItem('cart') || {});
    const count = Object.values(cart).reduce((sum, item) => sum + item.quantity, 0);
    const cartLink = document.getElementById('cart-link');

    if (cartLink) {
        cartLink.textContent = `Panier (${count})`;
    }
}
