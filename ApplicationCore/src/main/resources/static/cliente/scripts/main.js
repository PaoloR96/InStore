document.addEventListener('DOMContentLoaded', () => {
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
        const response = await fetch(`${API_BASE_URL}/info`);
        const cliente = await response.json();
        currentUsername = cliente.username;
        document.getElementById('usernameDisplay').textContent = currentUsername;
    } catch (error) {
        console.error('Errore nel caricamento delle informazioni del cliente:', error);
    }
}