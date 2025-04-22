// scripts/main.js
document.addEventListener("DOMContentLoaded", () => {
    loadRivenditoreInfo();
    loadProducts();
    document.getElementById("productForm").addEventListener("submit", addProduct);
});


async function loadRivenditoreInfo() {
    try {
        const response = await fetch(`${API_BASE_URL}/info`);
        const rivenditore = await response.json();
        currentUsername = rivenditore.username;
        document.getElementById('retailerName').textContent = currentUsername;
    } catch (error) {
        console.error('Errore nel caricamento delle informazioni del rivenditore:', error);
    }
}


