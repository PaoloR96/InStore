function enableFieldset() {
    var role = document.forms["registration"]["role"].value;
    var clienteAttributes = document.getElementById("clienteAttributes");
    var rivenditoreAttributes = document.getElementById("rivenditoreAttributes");

    if (role === "CLIENTE") {
        clienteAttributes.disabled = false;
        clienteAttributes.hidden = false;
        rivenditoreAttributes.disabled = true;
        rivenditoreAttributes.hidden = true;
    } else if (role === "RIVENDITORE") {
        clienteAttributes.disabled = true;
        clienteAttributes.hidden = true;
        rivenditoreAttributes.disabled = false;
        rivenditoreAttributes.hidden = false;
    } else {
        clienteAttributes.disabled = true;
        clienteAttributes.hidden = true;
        rivenditoreAttributes.disabled = true;
        rivenditoreAttributes.hidden = true;
    }
}

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