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

async function sendRequest(uri, method){
    let csrf_token = $("meta[name='_csrf']").attr("content");
    let csrf_header = $("meta[name='_csrf_header']").attr("content");

    return await fetch(uri, {
        method: method,
        headers: {
            [csrf_header]: csrf_token,
        }
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    updateCart();
    loadClientInfo();
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
    document.getElementById("user-profile-btn").addEventListener("click", () => {
        window.location.href='profile'
    });
    document.getElementById("logout-btn").addEventListener("click", () => {window.location.href='/logout'});
    document.getElementById("toggle-cart").addEventListener("click", toggleCart);
    document.getElementById("close-cart-btn").addEventListener("click", toggleCart);
    document.getElementById("refresh-btn").addEventListener("click", refreshStore);
    document.getElementById("premium-upgrade").addEventListener("click", upgradeToPremium);
    document.getElementById("refresh-cart-btn").addEventListener("click", updateCart);
    document.getElementById("checkout-btn").addEventListener("click", checkout);
});

