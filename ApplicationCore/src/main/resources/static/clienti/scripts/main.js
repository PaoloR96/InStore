document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('usernameDisplay').textContent = currentUsername;
    loadProducts();
    updateCart();
    loadClientInfo();
});

function refreshStore() {
    loadProducts();
    updateCart();
}

async function loadClientInfo() {
    try {
        const response = await fetch(`${API_BASE_URL}/clienti/${currentUsername}`);
        const cliente = await response.json();
        isPremiumClient = cliente.tipo_cliente === 'PREMIUM';
    } catch (error) {
        console.error('Errore nel caricamento delle informazioni del cliente:', error);
    }
}