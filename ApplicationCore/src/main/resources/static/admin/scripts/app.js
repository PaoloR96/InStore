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
        const btn = document.createElement("button");
        btn.innerHTML = `${user.enabled ? "Disabilita" : "Abilita"}`;
        btn.addEventListener("click", toggleUserStatus)
        btn.user = user.username;
        btn.enabled = user.enabled;

        row.innerHTML = `
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.numCell}</td>
            <td>${user.roles.join(", ")}</td>
            <td>${user.enabled ? "Abilitato" : "Disabilitato"}</td>
        `;

        const btn_column = document.createElement("td");
        btn_column.appendChild(btn);
        row.appendChild(btn_column);

        tableBody.appendChild(row);
    });
}

// Funzione per abilitare/disabilitare un utente
async function toggleUserStatus(evt) {
    try {
        const currentStatus = evt.currentTarget.enabled;
        const userId = evt.currentTarget.user;

        let api_url
        if(currentStatus) api_url = '/disable-user'
        else api_url = '/enable-user'

        let csrf_token = $("meta[name='_csrf']").attr("content");
        let csrf_header = $("meta[name='_csrf_header']").attr("content");

        await fetch(`${API_BASE_URL}${api_url}?` + new URLSearchParams(
            {
                username: userId
            }
        ).toString(), {
            method: 'PATCH',
            headers: {
                [csrf_header]: csrf_token
            }
        })

        await fetchUsers()
    } catch (error) {
        console.error("Errore nel cambiamento dello stato dell'utente:", error);
    }
}

// Funzione di logout
function logout() {
    window.location.href = '/logout';
}

// Recupera e mostra la lista degli utenti all'avvio
document.addEventListener('DOMContentLoaded', () => {
    fetchUsers();
    document.getElementById("logout-btn").addEventListener("click", logout);
});
