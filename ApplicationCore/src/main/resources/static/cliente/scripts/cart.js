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
        const response = await sendRequest(`${API_BASE_URL}/carrello/aggiungi?idProdotto=${productId}&quantita=${quantity}`, 'POST')

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
                <button class="remove-item-btn" data-prodotto="${item.idProdotto}" title="Rimuovi">
                    <i class="fas fa-trash-alt"></i>
                </button>
            </div>
        </div>
    `).join('');

    const buttons = Array.from(document.getElementsByClassName("remove-item-btn"));
    buttons.forEach( btn => {
            btn.addEventListener("click", removeFromCart);
        }
    );

    // Mostra le informazioni sullo sconto se presente
    if (tipoCliente === 'PREMIUM') {
        discountInfo.innerHTML = `
            <div>
                <span>Sconto Premium applicato:</span>
                <strong>-${discount}%</strong>
            </div>
        `;
    } else {
        discountInfo.innerHTML = ''; // Nasconde la sezione sconto se non presente
    }

    document.getElementById('cartTotal').textContent = totalPrice.toFixed(2);
}

async function removeFromCart(evt) {
    try {
        const productId = evt.currentTarget.getAttribute("data-prodotto");
        console.log('Rimozione prodotto con ID:', productId);

        // Usa la stessa logica di autenticazione delle altre richieste
        let csrf_token = $("meta[name='_csrf']").attr("content");
        let csrf_header = $("meta[name='_csrf_header']").attr("content");

        const response = await fetch(`${API_BASE_URL}/carrello/rimuovi?idProdotto=${productId}`, {
            method: 'DELETE',
            headers: {
                [csrf_header]: csrf_token,
                'Content-Type': 'application/json'
            }
        });

        console.log('Risposta rimozione:', response.status, response.statusText);

        if (response.ok) {
            console.log('Prodotto rimosso con successo');
            await updateCart();
        } else {
            const errorText = await response.text();
            console.error('Errore nella rimozione:', errorText);
            alert('Errore nella rimozione del prodotto: ' + errorText);
        }
    } catch (error) {
        console.error('Errore nella rimozione dal carrello:', error);
        alert('Si è verificato un errore. Riprova più tardi.');
    }
}

// Funzione per il checkout con PayPal basata sui prodotti del carrello
async function checkout() {
    try {
        // Verifica che il carrello non sia vuoto
        if (!cartItems || cartItems.length === 0) {
            alert('Il carrello è vuoto! Aggiungi alcuni prodotti prima di procedere al pagamento.');
            return;
        }

        // Disabilita il pulsante checkout per prevenire doppi click
        const checkoutBtn = document.getElementById('checkout-btn');
        const originalText = checkoutBtn.innerHTML;
        checkoutBtn.disabled = true;
        checkoutBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Creazione pagamento...';

        // Ottieni il totale attuale del carrello dal DOM
        const cartTotalElement = document.getElementById('cartTotal');
        const currentTotal = parseFloat(cartTotalElement.textContent);

        // Prepara i dati per la richiesta di pagamento
        const paymentData = {
            currency: 'EUR',
            method: 'paypal',
            intent: 'sale'
            // Non specifichiamo il total, lasciamo che il backend lo calcoli dal carrello
        };

        console.log('Invio richiesta di pagamento per importo carrello:', currentTotal);

        // Crea il pagamento PayPal
        const response = await sendRequestWithBody(`${API_BASE_URL}/pagamento/create`, 'POST', paymentData);

        if (response.ok) {
            const paymentResponse = await response.json();
            console.log('Risposta pagamento:', paymentResponse);

            // Reindirizza l'utente alla pagina di pagamento PayPal
            if (paymentResponse.approvalUrl) {
                console.log('Reindirizzamento a PayPal:', paymentResponse.approvalUrl);
                window.location.href = paymentResponse.approvalUrl;
            } else {
                throw new Error('URL di approvazione PayPal non ricevuto');
            }
        } else {
            const error = await response.text();
            console.error('Errore nella creazione del pagamento:', error);
            alert('Errore nella creazione del pagamento: ' + error);
        }
    } catch (error) {
        console.error('Errore durante il checkout:', error);
        alert('Si è verificato un errore durante il checkout. Riprova più tardi.');
    } finally {
        // Riabilita il pulsante checkout
        const checkoutBtn = document.getElementById('checkout-btn');
        if (checkoutBtn) {
            checkoutBtn.disabled = false;
            checkoutBtn.innerHTML = originalText || '<i class="fas fa-credit-card"></i> Procedi al pagamento';
        }
    }
}

// Funzione helper per inviare richieste con body JSON
async function sendRequestWithBody(uri, method, data) {
    let csrf_token = $("meta[name='_csrf']").attr("content");
    let csrf_header = $("meta[name='_csrf_header']").attr("content");

    return await fetch(uri, {
        method: method,
        headers: {
            [csrf_header]: csrf_token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
}

function toggleCart() {
    document.getElementById('cartModal').classList.toggle('active');
}

// Funzione per gestire il ritorno da PayPal
function handlePaymentReturn() {
    const urlParams = new URLSearchParams(window.location.search);
    const paymentId = urlParams.get('paymentId');
    const PayerID = urlParams.get('PayerID');

    if (paymentId && PayerID) {
        console.log('Ritorno da PayPal - PaymentID:', paymentId, 'PayerID:', PayerID);

        // Mostra un messaggio di elaborazione
        const processingMessage = document.createElement('div');
        processingMessage.style.cssText = `
            position: fixed; 
            top: 50%; 
            left: 50%; 
            transform: translate(-50%, -50%); 
            background: #007bff; 
            color: white; 
            padding: 20px; 
            border-radius: 8px; 
            z-index: 9999;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        `;
        processingMessage.innerHTML = `
            <i class="fas fa-spinner fa-spin"></i> 
            Elaborazione pagamento in corso...
        `;
        document.body.appendChild(processingMessage);

        // Il pagamento verrà completato automaticamente dal backend
        // Aggiorna il carrello dopo un breve delay per dare tempo al backend
        setTimeout(() => {
            updateCart();
            document.body.removeChild(processingMessage);
        }, 2000);
    }
}

// Chiamata per gestire il ritorno da PayPal quando la pagina si carica
document.addEventListener('DOMContentLoaded', function() {
    handlePaymentReturn();
});