async function loadProducts() {
    const productsGrid = document.getElementById("productsGrid");

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

    products.sort((a, b) => b.id - a.id);

    productsGrid.innerHTML = products.map(product => `
        <div class="product-card">
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

// Funzione per mostrare errore vicino al campo
function showError(inputId, message) {
    const field = document.getElementById(inputId);
    const errorEl = field.closest('.form-group').querySelector('.error-message');
    errorEl.textContent = message;
}

// Funzione per pulire tutti gli errori
function clearErrors(form) {
    form.querySelectorAll('.error-message').forEach(el => el.textContent = '');
}

// Validazione coerente con backend
function validateForm() {
    const nomeProdotto = document.getElementById('nomeProdotto').value.trim();
    const descrizione = document.getElementById('descrizione').value.trim();
    const prezzo = parseFloat(document.getElementById('prezzo').value);
    const taglia = document.getElementById('taglia').value;
    const quantitaTotale = parseInt(document.getElementById('quantitaTotale').value);
    const immagine = document.getElementById('immagineProdotto').files[0];

    let isValid = true;

    if (!nomeProdotto) {
        showError('nomeProdotto', 'Il nome del prodotto è obbligatorio.');
        isValid = false;
    } else if (nomeProdotto.length > 30) {
        showError('nomeProdotto', 'Il nome del prodotto non può superare i 30 caratteri.');
        isValid = false;
    }

    if (descrizione.length > 200) {
        showError('descrizione', 'La descrizione non può superare i 200 caratteri.');
        isValid = false;
    }

    if (isNaN(prezzo) || prezzo < 0) {
        showError('prezzo', 'Prezzo non valido (>= 0).');
        isValid = false;
    }

    if (!['XS', 'S', 'M', 'L', 'XL'].includes(taglia)) {
        showError('taglia', 'Seleziona una taglia valida.');
        isValid = false;
    }

    if (isNaN(quantitaTotale) || quantitaTotale < 0) {
        showError('quantitaTotale', 'La quantità deve essere un numero positivo o zero.');
        isValid = false;
    }

    if (!immagine) {
        showError('immagineProdotto', 'Carica un\'immagine del prodotto.');
        isValid = false;
    }

    return isValid;
}

async function addProduct(event) {
    event.preventDefault();

    const form = event.target;
    clearErrors(form);

    if (!validateForm()) return;

    const submitButton = form.querySelector('.submit-btn');
    const originalButtonText = submitButton.innerHTML;
    submitButton.disabled = true;
    submitButton.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Aggiunta in corso...';

    const prodotto = {
        nomeProdotto: document.getElementById('nomeProdotto').value.trim(),
        descrizione: document.getElementById('descrizione').value.trim(),
        prezzo: parseFloat(document.getElementById('prezzo').value),
        taglia: document.getElementById('taglia').value,
        quantitaTotale: parseInt(document.getElementById('quantitaTotale').value)
    };

    const formData = new FormData();
    formData.append('prodotto', new Blob([JSON.stringify(prodotto)], { type: 'application/json' }));
    formData.append('immagineProdotto', document.getElementById('immagineProdotto').files[0]);

    try {
        const csrf_token = $("meta[name='_csrf']").attr("content");
        const csrf_header = $("meta[name='_csrf_header']").attr("content");

        const response = await fetch(`${API_BASE_URL}/insprodotti`, {
            method: 'POST',
            headers: {
                [csrf_header]: csrf_token,
            },
            body: formData,
        });

        const responseText = await response.text();

        if (response.ok) {
            showSuccessAnimation(responseText || 'Prodotto aggiunto con successo!');
            alert(responseText || 'Prodotto aggiunto con successo!');
            refreshStore();
        } else {
            if (response.status === 400) {
                alert(responseText || 'Errore di validazione dell\'input. Controlla i campi inseriti.');
            } else {
                alert(responseText || 'Si è verificato un errore. Riprova più tardi.');
            }
        }
    } catch (error) {
        console.error('Errore nell\'aggiunta del prodotto:', error);
        showErrorMessage(error.message);
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

function refreshStore() {
    document.getElementById("productForm").reset();
    loadProducts();
}
