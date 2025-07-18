function enableFieldset() {
    const role = document.forms["registration"]["role"].value;
    const clienteAttributes = document.getElementById("clienteAttributes");
    const rivenditoreAttributes = document.getElementById("rivenditoreAttributes");

    if (role === "CLIENTE") {
        clienteAttributes.disabled = false;
        clienteAttributes.hidden = false;
        rivenditoreAttributes.disabled = true;
        rivenditoreAttributes.hidden = true;
        rivenditoreAttributes.querySelectorAll("input").forEach(input => input.value = "");
    } else if (role === "RIVENDITORE") {
        clienteAttributes.disabled = true;
        clienteAttributes.hidden = true;
        clienteAttributes.querySelectorAll("input").forEach(input => input.value = "");
        rivenditoreAttributes.disabled = false;
        rivenditoreAttributes.hidden = false;
    } else {
        clienteAttributes.disabled = true;
        clienteAttributes.hidden = true;
        clienteAttributes.querySelectorAll("input").forEach(input => input.value = "");
        rivenditoreAttributes.disabled = true;
        rivenditoreAttributes.hidden = true;
        rivenditoreAttributes.querySelectorAll("input").forEach(input => input.value = "");
    }
}

function showError(field, message) {
    const errorElement = document.createElement("div");
    errorElement.className = "error-message";
    errorElement.setAttribute("aria-live", "polite");
    errorElement.textContent = message;
    field.parentNode.appendChild(errorElement);
}

function clearErrors() {
    document.querySelectorAll(".error-message").forEach(el => el.remove());
}

function validatePartitaIva(partitaIva) {
    if (!/^\d{11}$/.test(partitaIva)) {
        return false;
    }

    let sum = 0;
    for (let i = 0; i < 10; i++) {
        let digit = parseInt(partitaIva[i], 10);
        if (i % 2 === 0) {
            sum += digit;
        } else {
            let doubled = digit * 2;
            sum += doubled > 9 ? doubled - 9 : doubled;
        }
    }

    let checkDigit = parseInt(partitaIva[10], 10);
    let calculatedCheckDigit = (10 - (sum % 10)) % 10;

    return checkDigit === calculatedCheckDigit;
}

document.addEventListener("DOMContentLoaded", function () {
    const roleFieldset = document.getElementById("roleChoose");
    if (roleFieldset) {
        roleFieldset.addEventListener("change", enableFieldset);
    }

    const logoutButton = document.querySelector(".logout-button");
    if (logoutButton) {
        logoutButton.addEventListener("click", function () {
            window.location.href = "/logout";
        });
    }

    const form = document.forms["registration"];
    if (form) {
        // Validazione in tempo reale per Partita IVA
        form["partitaIva"].addEventListener("blur", function () {
            clearErrors();
            const partitaIva = this.value.trim();
            if (partitaIva && !/^\d{11}$/.test(partitaIva)) {
                showError(this, "La Partita IVA deve contenere esattamente 11 cifre numeriche.");
            } else if (partitaIva && !validatePartitaIva(partitaIva)) {
                showError(this, "La Partita IVA non è valida: controllo del checksum fallito.");
            }
        });

        // Validazione in tempo reale per IBAN
        form["iban"].addEventListener("blur", function () {
            clearErrors();
            const iban = this.value.trim();
            const normalizedIban = iban.replace(/\s/g, "");
            if (iban && !window.IBAN?.isValid(normalizedIban)) {
                showError(this, "L'IBAN non è in un formato valido. Assicurati di inserire un IBAN corretto, ad esempio: IT60X0542811101000000123456");
            }
        });

        form.addEventListener("submit", function (e) {
            clearErrors();
            const role = form["role"].value;
            const messages = [];

            if (role === "CLIENTE") {
                const nome = form["nome"].value.trim();
                const cognome = form["cognome"].value.trim();

                if (!nome) {
                    messages.push("Il nome è obbligatorio.");
                    showError(form["nome"], "Il nome è obbligatorio.");
                } else if (nome.length > 50) {
                    messages.push("Il nome non può superare i 50 caratteri.");
                    showError(form["nome"], "Il nome non può superare i 50 caratteri.");
                }

                if (!cognome) {
                    messages.push("Il cognome è obbligatorio.");
                    showError(form["cognome"], "Il cognome è obbligatorio.");
                } else if (cognome.length > 50) {
                    messages.push("Il cognome non può superare i 50 caratteri.");
                    showError(form["cognome"], "Il cognome non può superare i 50 caratteri.");
                }
            } else if (role === "RIVENDITORE") {
                const nomeSocieta = form["nomeSocieta"].value.trim();
                const partitaIva = form["partitaIva"].value.trim();
                const iban = form["iban"].value.trim();
                const normalizedIban = iban.replace(/\s/g, "");

                if (!nomeSocieta) {
                    messages.push("Il nome della società è obbligatorio.");
                    showError(form["nomeSocieta"], "Il nome della società è obbligatorio.");
                } else if (nomeSocieta.length > 100) {
                    messages.push("Il nome della società non può superare i 100 caratteri.");
                    showError(form["nomeSocieta"], "Il nome della società non può superare i 100 caratteri.");
                }

                if (!partitaIva) {
                    messages.push("La Partita IVA è obbligatoria.");
                    showError(form["partitaIva"], "La Partita IVA è obbligatoria.");
                } else if (!/^\d{11}$/.test(partitaIva)) {
                    messages.push("La Partita IVA deve contenere esattamente 11 cifre numeriche.");
                    showError(form["partitaIva"], "La Partita IVA deve contenere esattamente 11 cifre numeriche.");
                } else if (!validatePartitaIva(partitaIva)) {
                    messages.push("La Partita IVA non è valida: controllo del checksum fallito.");
                    showError(form["partitaIva"], "La Partita IVA non è valida: controllo del checksum fallito.");
                }

                if (!iban) {
                    messages.push("L'IBAN è obbligatorio.");
                    showError(form["iban"], "L'IBAN è obbligatorio.");
                } else if (!window.IBAN) {
                    messages.push("Errore: validazione IBAN non disponibile. Controlla la connessione alla rete.");
                    showError(form["iban"], "Errore: validazione IBAN non disponibile.");
                } else if (!window.IBAN.isValid(normalizedIban)) {
                    messages.push("L'IBAN non è in un formato valido. Assicurati di inserire un IBAN corretto, ad esempio: IT60X0542811101000000123456");
                    showError(form["iban"], "L'IBAN non è in un formato valido.");
                }
            } else {
                messages.push("Devi selezionare un ruolo.");
                showError(form["role"], "Devi selezionare un ruolo.");
            }

            if (messages.length > 0) {
                e.preventDefault();
            }
        });
    }
});

//const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

// function clienteRegistration(){
//     fetch("/api/cliente-registration", {
//         method: "POST",
//         headers: new Headers({
//             "Contert-Type": "application/x-www-form-urlencoded",
//             "X-XSRF-TOKEN": csrfToken
//         }),
//         body: new FormData(document.forms["registration"])
//     })
//     .then(response => {
//         // Gestisci la risposta qui
//         console.log("Registrazione cliente completata:", response);
//     })
//     .catch(error => {
//         console.error("Errore durante la registrazione cliente:", error);
//     });
// }
//
// function rivenditoreRegistration(form){
//     fetch("/api/rivenditore-registration", {
//         method: "POST",
//         headers: new Headers({
//             "Contert-Type": "application/x-www-form-urlencoded",
//             "X-XSRF-TOKEN": csrfToken
//         }),
//         body: new FormData(form)
//     })
//     .then(response => {
//         // Gestisci la risposta qui
//         console.log("Registrazione rivenditore completata:", response);
//     })
//     .catch(error => {
//         console.error("Errore durante la registrazione rivenditore:", error);
//     });
// }

// Nota: Le funzioni di registrazione vengono chiamate direttamente dall'attributo
// 'formaction' dei bottoni submit. Se volessi gestirle via JavaScript,
// dovresti aggiungere un event listener al form e prevenire il comportamento
// di default del submit.