async function loadProducts() {
    try {
        const response = await fetch(`${API_BASE_URL}/prodotti`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error('Errore nel caricamento dei prodotti:', error);
    }
}

function displayProducts(products) {
    const grid = document.getElementById('productsGrid');
    grid.innerHTML = products.map(product => `
        <div class="product-card">
            <img src="${product.pathImmagine || '/placeholder.jpg'}" alt="${product.nomeProdotto}" class="product-image">
            <div class="product-info">
                <h3 class="product-title">${product.nomeProdotto}</h3>
                <p class="product-price">€${product.prezzo.toFixed(2)}</p>
                <div class="product-size">
                    <span>Taglia:</span>
                    <span class="size-badge">${product.taglia}</span>
                </div>
                <p class="product-description">${product.descrizione || ''}</p>
                <div class="quantity-selector">
                    <input type="number" min="1" max="${product.quantitaTotale}" value="1" id="qty-${product.idProdotto}">
                    <button onclick="addToCart(${product.idProdotto})">Aggiungi al carrello</button>
                </div>
            </div>
        </div>
    `).join('');
}