// scripts/main.js
document.addEventListener("DOMContentLoaded", () => {
    loadRivenditoreInfo();
    loadProducts();
    const store_branding = document.getElementById("store-branding");
    store_branding.addEventListener("click", refreshStore);
    store_branding.addEventListener("mouseover", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.1)'
    });
    store_branding.addEventListener("mouseout", (evt) => {
        evt.currentTarget.style.backgroundColor='transparent'
    });
    store_branding.addEventListener("mousedown", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.2)'
    });
    store_branding.addEventListener("mouseup", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.1)'
    });
    document.getElementById("user-profile-btn").addEventListener("click", () => {window.location.href='profile'});
    document.getElementById("logout-btn").addEventListener("click", () => {window.location.href='/logout'});
    document.getElementById("refresh-btn").addEventListener("click", loadProducts);

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


