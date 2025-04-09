// URL base dell'API REST
const API_BASE_URL = "https://instore.puntoitstore.it/admin/api";

// Funzione per recuperare la lista degli utenti dal backend
async function fetchUsers() {
    try {
        const response = await fetch(API_BASE_URL + "/get-users");
        const users = await response.json();
        renderUsers(users);
    } catch (error) {
        console.error("Errore nel recupero degli utenti:", error);
    }
}

// Funzione per mostrare gli utenti nella tabella
function renderUsers(users) {
    const tableBody = document.querySelector("#usersTable tbody");
    tableBody.innerHTML = ""; // Pulizia della tabella

    users.forEach(user => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.numCell}</td>
            <td>${user.roles.join(", ")}</td>
            <td>${user.enabled ? "Abilitato" : "Disabilitato"}</td>
            <td>
                <button onclick="toggleUserStatus('${user.username}', ${user.enabled})">
                    ${user.enabled ? "Disabilita" : "Abilita"}
                </button>
            </td>
        `;

        tableBody.appendChild(row);
    });
}

// Funzione per abilitare/disabilitare un utente
async function toggleUserStatus(userId, currentStatus) {
    try {
        let api_url
        if(currentStatus) api_url = '/disable-user'
        else api_url = '/enable-user'

        await fetch(`${API_BASE_URL}${api_url}?` + new URLSearchParams(
            {
                username: userId
            }
        ).toString())

        fetchUsers()
    } catch (error) {
        console.error("Errore nel cambiamento dello stato dell'utente:", error);
    }
}

// Recupera e mostra la lista degli utenti all'avvio
fetchUsers()
