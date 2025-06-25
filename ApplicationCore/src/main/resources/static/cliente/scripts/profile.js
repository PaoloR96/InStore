async function loadUserData() {
    try {
        const [infoResponse, tipoResponse] = await Promise.all([
            fetch(`${API_BASE_URL}/info`),
            fetch(`${API_BASE_URL}/tipo`)
        ]);

        // Controllo risposte
        if (!infoResponse.ok) {
            throw new Error(`Errore HTTP ${infoResponse.status} durante il caricamento delle info utente`);
        }

        if (!tipoResponse.ok) {
            throw new Error(`Errore HTTP ${tipoResponse.status} durante il caricamento del tipo utente`);
        }

        // Parsing delle risposte
        const userData = await infoResponse.json();
        const userTipo = await tipoResponse.text();

        displayUserData(userData, userTipo);

    } catch (error) {
        console.error('Errore:', error.message);
        alert(`Si è verificato un errore: ${error.message}`);
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

function formatPhoneNumber(phoneNumber) {
    if (!phoneNumber) return '';
    if (phoneNumber.length === 10) {
        return phoneNumber.replace(/(\d{3})(\d{3})(\d{4})/, '$1 $2 $3');
    } else if (phoneNumber.startsWith('+39') && phoneNumber.length === 13) {
        return phoneNumber.replace(/\+39(\d{3})(\d{3})(\d{4})/, '+39 $1 $2 $3');
    }
    return phoneNumber;
}

function toggleMobileMenu() {
    const mainMenu = document.querySelector('.main-menu');
    mainMenu.classList.toggle('active');
}

function logout() {
    window.location.href = '/logout';
}

document.addEventListener('DOMContentLoaded', () => {
    loadUserData();

    const storeBranding = document.getElementById("store-branding");
    storeBranding.addEventListener("click", () => window.location.href = 'index');
    storeBranding.addEventListener("mouseover", evt => {
        evt.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.1)';
    });
    storeBranding.addEventListener("mouseout", evt => {
        evt.currentTarget.style.backgroundColor = 'transparent';
    });
    storeBranding.addEventListener("mousedown", evt => {
        evt.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.2)';
    });
    storeBranding.addEventListener("mouseup", evt => {
        evt.currentTarget.style.backgroundColor = 'rgba(255,255,255,0.1)';
    });

    document.getElementById("logout-btn").addEventListener("click", logout);
    document.getElementById("mobile-menu-toggle").addEventListener("click", toggleMobileMenu);
    document.getElementById("refresh-btn").addEventListener("click", loadUserData);
    document.getElementById("edit-profile-btn").addEventListener("click", () => {
        alert('Funzionalità di modifica non ancora implementata');
    });
    document.getElementById("change-pwd-btn").addEventListener("click", () => {
        alert('Funzionalità di cambio password non ancora implementata');
    });
});
