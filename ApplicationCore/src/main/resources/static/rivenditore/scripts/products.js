async function loadProducts() {
    const productsGrid = document.getElementById("productsGrid");

    // Mostra stato di caricamento
    productsGrid.innerHTML = `
        <div class="products-grid">
            <i class="fas fa-spinner fa-spin fa-2x"></i>
            <p>Caricamento prodotti...</p>
        </div>
    `;

    try {
        const response = await fetch(`${API_BASE_URL}/listaprodotti`);
        const products = await response.json();
        displayProducts(products);
    } catch (error) {
        console.error("Errore nel caricamento prodotti:", error);
        productsGrid.innerHTML = `
            <div class="products-grid error">
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
            <div class="products-grid">
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
<!--            TODO non funziona l'upload delle immagini-->
            <img src="${product.pathImmagine}" alt="${product.nomeProdotto}" class="product-image">
            <div class="product-info">
                <h3 class="product-title">${product.nomeProdotto}</h3>
                <p class="product-price">€${product.prezzo.toFixed(2)}</p>
                <div class="product-size">
                    <span>Taglia:</span>
                    <span class="size-badge">${product.taglia}</span>
                </div>
                <p class="product-description">${product.descrizione || ''}</p>
                <div class="quantity-selector">
                    <span><strong>Quantità disponibile:</strong> ${product.quantitaTotale}</span>
                </div>
            </div>
        </div>
    `).join('');
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

    try {
        let csrf_token = $("meta[name='_csrf']").attr("content");
        let csrf_header = $("meta[name='_csrf_header']").attr("content");

        const response = await fetch(`${API_BASE_URL}/insprodotti`, {
            method: "POST",
            headers: {
                [csrf_header]: csrf_token,
                "Content-Type": "application/json"
            },
            body: JSON.stringify(nuovoProdotto)
        });

        if (response.ok) {

            const successMessage = await response.text();
            showSuccessAnimation(successMessage)
            alert(successMessage || 'Prodotto aggiunto con successo!');
            refreshStore();
        } else {
            const errorMessage = await response.text();
                    if (response.status === 400) {
                        alert(errorMessage && "Errore di validazione dell'input. Controlla i campi inseriti.");
                    } else {
                        alert(errorMessage && "Si è verificato un errore. Riprova più tardi.");
                    }
        }

    } catch (errorMessage) {
        console.error('Errore nell\'aggiunta del prodotto:', errorMessage);
        showErrorMessage(errorMessage.message);
        alert('Si è verificato un errore imprevisto. Riprova più tardi.');
    } finally {
        submitButton.disabled = false;
        submitButton.innerHTML = originalButtonText;
    }
}

function showSuccessAnimation(message = "Operazione completata con successo!") {
    const successDiv = document.createElement("div");
    successDiv.className = "success-animation";
    successDiv.innerHTML = `
        <i class="fas fa-check-circle fa-3x success-text"></i>
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
        <i class="fas fa-times-circle fa-3x error-msg"></i>
        <p>${message}</p>
    `;
    document.body.appendChild(errorDiv);

    setTimeout(() => {
        errorDiv.remove();
    }, 3000);
}

function refreshStore(){
    document.getElementById("productForm").reset();
    loadProducts();
}