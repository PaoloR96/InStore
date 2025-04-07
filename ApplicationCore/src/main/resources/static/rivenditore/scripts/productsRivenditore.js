/ Configurazione centralizzata dell'API
const API_CONFIG = {
    basePath: "/api",
    retailerId: "rivenditore2"
};

// Costruisce l'URL base completo
const API_BASE_URL = `${API_CONFIG.basePath}/rivenditori/${API_CONFIG.retailerId}`;

document.addEventListener("DOMContentLoaded", () => {
    loadRetailerName();
    loadProducts();
    document.getElementById("productForm").addEventListener("submit", addProduct);
});

// Funzione per effettuare richieste API
async function fetchAPI(endpoint, options = {}) {
    try {
        const url = `${API_BASE_URL}${endpoint}`;
        const response = await fetch(url, options);

        if (!response.ok) {
            throw new Error(`Errore HTTP: ${response.status}`);
        }

        return await response.json();
    } catch (error) {
        console.error(`Errore nella richiesta API: ${error.message}`);
        throw error;
    }
}

// Funzione per estrarre e visualizzare il nome del rivenditore
function loadRetailerName() {
    const formattedName = API_CONFIG.retailerId.charAt(0).toUpperCase() + API_CONFIG.retailerId.slice(1);
    document.getElementById("retailerName").textContent = formattedName;
}

// Funzione per caricare i prodotti
async function loadProducts() {
    const productsGrid = document.getElementById("productsGrid");

    // Mostra stato di caricamento
    productsGrid.innerHTML = `
        <div style="grid-column: 1/-1; text-align: center; padding: 2rem;">
            <i class="fas fa-spinner fa-spin fa-2x"></i>
            <p>Caricamento prodotti...</p>
        </div>
    `;

    try {
        const products = await fetchAPI("/listaprodotti");
        displayProducts(products);
    } catch (error) {
        console.error("Errore nel caricamento prodotti:", error);
        productsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 2rem; color: var(--accent-color);">
                <i class="fas fa-exclamation-circle fa-2x"></i>
                <p>Errore nel caricamento dei prodotti</p>
            </div>
        `;
    }
}

// Funzione per visualizzare i prodotti
function displayProducts(products) {
    const productsGrid = document.getElementById("productsGrid");
    productsGrid.innerHTML = "";

    if (products.length === 0) {
        productsGrid.innerHTML = `
            <div style="grid-column: 1/-1; text-align: center; padding: 2rem;">
                <i class="fas fa-box-open fa-2x"></i>
                <p>Nessun prodotto disponibile</p>
            </div>
        `;
        return;
    }

    // Ordina i prodotti per ID in ordine decrescente
    products.sort((a, b) => b.id - a.id);

    productsGrid.innerHTML = products.map(product => `
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
                    <input type="number" min="1" max="${product.quantitaTotale}" value="1" id="qty-${product.id || product.idProdotto}">
                    <button onclick="addToCart(${product.id || product.idProdotto})">Aggiungi al carrello</button>
                </div>
            </div>
        </div>
    `).join('');
}

// Funzione per aggiungere un prodotto al carrello (stub)
function addToCart(productId) {
    const quantity = document.getElementById(`qty-${productId}`).value;
    console.log(`Aggiunto al carrello: Prodotto ID ${productId}, Quantità ${quantity}`);
    showSuccessAnimation("Prodotto aggiunto al carrello!");
}

// Funzione per aggiungere un prodotto
async function addProduct(event) {
    event.preventDefault();

    const submitButton = event.target.querySelector('.submit-btn');
    const originalButtonText = submitButton.innerHTML;
    submitButton.disabled = true;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Aggiunta in corso...';

    const nuovoProdotto = {
        nomeProdotto: document.getElementById("nomeProdotto").value,
        descrizione: document.getElementById("descrizione").value,
        prezzo: parseFloat(document.getElementById("prezzo").value),
        taglia: document.getElementById("taglia").value,
        pathImmagine: document.getElementById("pathImmagine").value || '/placeholder.jpg',
        quantitaTotale: parseInt(document.getElementById("quantitaTotale").value),
    };

    try {
        await fetchAPI("/insprodotti", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuovoProdotto)
        });

        showSuccessAnimation("Prodotto aggiunto con successo!");
        document.getElementById("productForm").reset();
        loadProducts();
    } catch (error) {
        console.error("Errore nell'aggiunta del prodotto:", error);
        showErrorMessage("Errore nell'aggiunta del prodotto");
    }

    submitButton.disabled = false;
    submitButton.innerHTML = originalButtonText;
}

function showSuccessAnimation(message = "Operazione completata con successo!") {
    const successDiv = document.createElement("div");
    successDiv.className = "success-animation";
    successDiv.innerHTML = `
        <i class="fas fa-check-circle fa-3x" style="color: var(--success-color); margin-bottom: 1rem;"></i>
        <p>${message}</p>
    `;
    document.body.appendChild(successDiv);

    setTimeout(() => {
        successDiv.remove();
    }, 2000);
}

function showErrorMessage(message) {
    const errorDiv = document.createElement("div");
    errorDiv.className = "error-animation";
    errorDiv.innerHTML = `
        <i class="fas fa-times-circle fa-3x" style="color: var(--accent-color); margin-bottom: 1rem;"></i>
        <p>${message}</p>
    `;
    document.body.appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.remove();
    }, 3000);
}
