// URL base dell'API REST
const API_BASE_URL = "https://instore.puntoitstore.it/admin/api";

// Funzione per recuperare la lista degli utenti dal backend
async function fetchClients() {
    try {
        const response = await fetch(API_BASE_URL + "/get-clienti");
        const users = await response.json();
        renderClients(users);
    } catch (error) {
        console.error("Errore nel recupero degli utenti:", error);
    }
}

async function fetchSellers() {
    try {
        const response = await fetch(API_BASE_URL + "/get-rivenditori");
        const users = await response.json();
        renderSellers(users);
    } catch (error) {
        console.error("Errore nel recupero degli utenti:", error);
    }
}

// Funzione per mostrare gli utenti nella tabella
function renderClients(users) {
    const tableBody = document.querySelector("#clientsTable tbody");
    tableBody.innerHTML = ""; // Pulizia della tabella

    users.forEach(user => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.numCell}</td>
            <td>${user.nome}</td>
            <td>${user.cognome}</td>
            <td>${user.isActive ? "Abilitato" : "Disabilitato"}</td>
            <td>
                <button onclick="toggleUserStatus(${user.id}, ${user.isActive}, true)">
                    ${user.isActive ? "Disabilita" : "Abilita"}
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

function renderSellers(users) {
    const tableBody = document.querySelector("#sellersTable tbody");
    tableBody.innerHTML = ""; // Pulizia della tabella

    users.forEach(user => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.numCell}</td>
            <td>${user.nomeSocieta}</td>
            <td>${user.partitaIva}</td>
            <td>${user.iban}</td>
            <td>${user.isActive ? "Abilitato" : "Disabilitato"}</td>
            <td>
                <button onclick="toggleUserStatus(${user.id}, ${user.isActive}, false)">
                    ${user.isActive ? "Disabilita" : "Abilita"}
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

// Funzione per abilitare/disabilitare un utente
async function toggleUserStatus(userId, currentStatus, isClient) {
    try {
        let api_url
        if(currentStatus) api_url = '/disable-user'
        else api_url = '/enable-user'
        await fetch(`${API_BASE_URL}${api_url}?` + new URLSearchParams(
            {
                username: `${userId}`
            }
        ).toString())
        if(isClient) fetchClients(); // Aggiorna la lista degli utenti
        else fetchSellers();
    } catch (error) {
        console.error("Errore nel cambiamento dello stato dell'utente:", error);
    }
}

// Recupera e mostra la lista degli utenti all'avvio
fetchClients();
fetchSellers();
