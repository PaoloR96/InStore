document.addEventListener("DOMContentLoaded", () => {
    loadRetailerName();
    loadProducts();
    document.getElementById("productForm").addEventListener("submit", addProduct);
});

// Funzione per provare più porte
async function tryFetchFromMultiplePorts(paths) {
    const ports = [80, 8081];
    for (let port of ports) {
        try {
            const response = await fetch(`http://localhost:${port}${paths}`);
            if (response.ok) {
                return await response.json();
            }
        } catch (error) {
            console.warn(`Errore su porta ${port}:`, error);
        }
    }
    throw new Error("Nessuna connessione disponibile");
}

// Funzione per estrarre e visualizzare il nome del rivenditore
function loadRetailerName() {
    const apiUrl = "/api/rivenditori/rivenditore2/listaprodotti";
    const retailerId = "rivenditore2";
    const formattedName = retailerId.charAt(0).toUpperCase() + retailerId.slice(1);

    document.getElementById("retailerName").textContent = formattedName;
}

// Funzione per caricare i prodotti
async function loadProducts() {
    const productsGrid = document.getElementById("productsGrid");
    productsGrid.innerHTML = `
        <div style="grid-column: 1/-1; text-align: center; padding: 2rem;">
            <i class="fas fa-spinner fa-spin fa-2x"></i>
            <p>Caricamento prodotti...</p>
        </div>
    `;

    try {
        const data = await tryFetchFromMultiplePorts("/api/rivenditori/rivenditore2/listaprodotti");
        productsGrid.innerHTML = "";

        if (data.length === 0) {
            productsGrid.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 2rem;">
                    <i class="fas fa-box-open fa-2x"></i>
                    <p>Nessun prodotto disponibile</p>
                </div>
            `;
            return;
        }

        data.sort((a, b) => b.id - a.id);
        data.forEach(product => {
            productsGrid.appendChild(createProductCard(product));
        });

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
        pathImmagine: document.getElementById("pathImmagine").value,
        quantitaTotale: parseInt(document.getElementById("quantitaTotale").value),
    };

    const endpoints = [
        "http://localhost:80/api/rivenditori/rivenditore2/insprodotti",
        "http://localhost:8081/api/rivenditori/rivenditore2/insprodotti"
    ];

    let success = false;
    for (let url of endpoints) {
        try {
            const response = await fetch(url, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(nuovoProdotto)
            });

            if (response.ok) {
                success = true;
                break;
            }
        } catch (error) {
            console.warn(`Errore su ${url}:`, error);
        }
    }

    if (success) {
        showSuccessAnimation();
        document.getElementById("productForm").reset();
        loadProducts();
    } else {
        showErrorMessage("Errore nell'aggiunta del prodotto");
    }

    submitButton.disabled = false;
    submitButton.innerHTML = originalButtonText;
}

function createProductCard(product) {
    const productCard = document.createElement("div");
    productCard.classList.add("product-card");

    productCard.innerHTML = `
        <img src="${product.pathImmagine}" alt="${product.nomeProdotto}" class="product-image">
        <div class="product-details">
            <h3 class="product-title">${product.nomeProdotto}</h3>
            <p class="product-price">€${product.prezzo.toFixed(2)}</p>
            <div class="product-meta">
                <span><i class="fas fa-tshirt"></i> ${product.taglia}</span>
                <span><i class="fas fa-cubes"></i> ${product.quantitaTotale}</span>
            </div>
        </div>
    `;

    return productCard;
}

function showSuccessAnimation() {
    const successDiv = document.createElement("div");
    successDiv.className = "success-animation";
    successDiv.innerHTML = `
        <i class="fas fa-check-circle fa-3x" style="color: var(--success-color); margin-bottom: 1rem;"></i>
        <p>Prodotto aggiunto con successo!</p>
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
