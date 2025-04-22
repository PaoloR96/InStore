async function loadUserData() {

    try {
        const response = await fetch(`${API_BASE_URL}/info`);
        const tipoResponse = await fetch(`${API_BASE_URL}/tipo`);

        const userData = await response.json();
        const userTipo = await tipoResponse.text();

        displayUserData(userData, userTipo);

    } catch (error) {
        console.error('Errore:', error);
    }
}

function displayUserData(userData, userTipo) {

    document.getElementById('fullName').textContent = `${userData.nome} ${userData.cognome}`;
    document.getElementById('usernameDisplay').textContent = userData.username;
    document.getElementById('username').textContent = userData.username;
    document.getElementById('email').textContent = userData.email;
    document.getElementById('nome').textContent = userData.nome;
    document.getElementById('cognome').textContent = userData.cognome;
    document.getElementById('numCell').textContent = formatPhoneNumber(userData.numCell);
    document.getElementById('tipoCliente').textContent = userTipo;

}

// Formatta il numero di telefono per una migliore leggibilità
function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return '';

    // Assumiamo un formato italiano standard
    if (phoneNumber.length === 10) {
        return phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1 $2 $3');
    } else if (phoneNumber.startsWith('+39') && phoneNumber.length === 13) {
        return phoneNumber.replace(/\+39(\d{3})(\d{3})(\d{4})/, '+39 $1 $2 $3');
    }

    return phoneNumber;
}



// Funzione per il toggle del menu mobile
function toggleMobileMenu() {
    const mainMenu = document.querySelector('.main-menu');
    mainMenu.classList.toggle('active');
}

// Funzione di logout
function logout() {
    window.location.href = '/logout';
}

// Inizializza la pagina quando il documento è pronto
document.addEventListener('DOMContentLoaded', () => {
        loadUserData();
});