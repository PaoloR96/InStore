async function updateCart() {
    try {
        const response = await fetch(`${API_BASE_URL}/carrello/prodotti`);
        const tipoResponse = await fetch(`${API_BASE_URL}/tipo`);

        const cartResponse = await response.json();
        const userTipo = await tipoResponse.text();

        cartItems = cartResponse.prodotti;
        const totalItems = cartItems.reduce((sum, item) => sum + item.quantitaTotale, 0);
        document.getElementById('cartCount').textContent = totalItems;

        displayCartItems(cartResponse.prezzoTotale, cartResponse.scontoApplicato, userTipo);
    } catch (error) {
        console.error('Errore nell\'aggiornamento del carrello:', error);
    }
}

async function addToCart(productId) {
    const quantityInput = document.getElementById(`qty-${productId}`);
    const quantity = parseInt(quantityInput.value);

    try {
        // const response = await fetch(`${API_BASE_URL}/carrello/aggiungi?idProdotto=${productId}&quantita=${quantity}`, {
        //     method: 'POST'
        // });

        const response = sendRequest(`${API_BASE_URL}/carrello/aggiungi?idProdotto=${productId}&quantita=${quantity}`, 'POST')

        if (response.ok) {
            updateCart();
            alert('Prodotto aggiunto al carrello!');
        } else {
            const error = await response.text();
            alert(error);
        }
    } catch (error) {
        console.error('Errore nell\'aggiunta al carrello:', error);
        alert('Si è verificato un errore. Riprova più tardi.');
    }
}

function displayCartItems(totalPrice, discount, tipoCliente) {
    const cartItemsContainer = document.getElementById('cartItems');
    const discountInfo = document.getElementById('discountInfo');

    cartItemsContainer.innerHTML = cartItems.map(item => `
        <div class="cart-item">
            <img src="${item.pathImmagine || '/placeholder.jpg'}" alt="${item.nomeProdotto}" class="cart-item-image">
            <div class="cart-item-details">
                <span class="cart-item-title">${item.nomeProdotto}</span>
                <span class="cart-item-price">€${item.prezzo.toFixed(2)} x ${item.quantitaTotale}</span>
                <small>Taglia: ${item.taglia}</small>
            </div>
            <div class="cart-item-actions">
                <button class="remove-item-btn" onclick="removeFromCart(${item.idProdotto})" title="Rimuovi">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        </div>
    `).join('');

    // Mostra le informazioni sullo sconto se presente
    if (tipoCliente === 'PREMIUM') {
        discountInfo.innerHTML = `
            <div style="display: flex; justify-content: space-between; align-items: center;">
                <span>Sconto Premium applicato:</span>
                <strong>-${discount}%</strong>
            </div>
        `;
    } else {
        discountInfo.innerHTML = ''; // Nasconde la sezione sconto se non presente
    }

    document.getElementById('cartTotal').textContent = totalPrice.toFixed(2);
}

async function removeFromCart(productId) {
    try {
        // const response = await fetch(`${API_BASE_URL}/carrello/rimuovi?idProdotto=${productId}`, {
        //     method: 'DELETE'
        // });

        const response = sendRequest(`${API_BASE_URL}/carrello/rimuovi?idProdotto=${productId}`, 'DELETE')

        if (response.ok) {
            updateCart();
        } else {
            alert('Errore nella rimozione del prodotto');
        }
    } catch (error) {
        console.error('Errore nella rimozione dal carrello:', error);
        alert('Si è verificato un errore. Riprova più tardi.');
    }
}

async function checkout() {
    try {
        // const response = await fetch(`${API_BASE_URL}/pagamento`, {
        //     method: 'POST'
        // });

        const response = sendRequest(`${API_BASE_URL}/pagamento`, 'POST')

        if (response.ok) {
            alert('Pagamento effettuato con successo!');
            updateCart();
            toggleCart();
        } else {
            const error = await response.text();
            alert(error);
        }
    } catch (error) {
        console.error('Errore durante il pagamento:', error);
        alert('Si è verificato un errore durante il pagamento. Riprova più tardi.');
    }
}

function toggleCart() {
    document.getElementById('cartModal').classList.toggle('active');
}