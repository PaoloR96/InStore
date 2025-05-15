async function loadUserData() {

    try {
        const response = await fetch(`${API_BASE_URL}/info`);

        const userData = await response.json();

        displayUserData(userData);

    } catch (error) {
        console.error('Errore:', error);
    }
}

function displayUserData(userData) {
    document.getElementById('fullName').textContent = userData.nomeSocieta;
    document.getElementById('usernameDisplay').textContent = userData.username;
    document.getElementById('username').textContent = userData.username;
    document.getElementById('email').textContent = userData.email;
    document.getElementById('nomeSocieta').textContent = userData.nomeSocieta;
    document.getElementById('partitaIva').textContent = userData.partitaIva;
    document.getElementById('numCell').textContent = formatPhoneNumber(userData.numCell);
    document.getElementById('iban').textContent = userData.iban;

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
    const store_branding = document.getElementById("store-branding");
    store_branding.addEventListener("click", () => {window.location.href='index'});
    store_branding.addEventListener("mouseover", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.1)'
    });
    store_branding.addEventListener("mouseout", (evt) => {
        evt.currentTarget.style.backgroundColor='transparent'
    });
    store_branding.addEventListener("mousedown", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.2)'
    });
    store_branding.addEventListener("mouseup", (evt) => {
        evt.currentTarget.style.backgroundColor='rgba(255,255,255,0.1)'
    });
    document.getElementById("profile-div").addEventListener("click", () => {window.location.href='profile'});
    document.getElementById("logout-btn").addEventListener("click", logout);
    document.getElementById("mobile-menu-div").addEventListener("click", toggleMobileMenu);
    document.getElementById("refresh-btn").addEventListener("click", loadUserData);
    document.getElementById("edit-profile-btn").addEventListener("click", () => {alert('Funzionalità di modifica non ancora implementata')});
    document.getElementById("change-pwd-btn").addEventListener("click", () => {alert('Funzionalità di cambio password non ancora implementata')});

});