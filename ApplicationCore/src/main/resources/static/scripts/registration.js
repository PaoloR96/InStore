function enableFieldset() {
    var a = document.forms["registration"]["role"].value;

    if (a === "CLIENTE") {
        document.getElementById("clienteAttributes").disabled = false;
        document.getElementById("clienteAttributes").hidden = false;
        document.getElementById("rivenditoreAttributes").disabled = true;
        document.getElementById("rivenditoreAttributes").hidden = true;
    } else if (a === "RIVENDITORE") {
        document.getElementById("clienteAttributes").disabled = true;
        document.getElementById("clienteAttributes").hidden = true;
        document.getElementById("rivenditoreAttributes").disabled = false;
        document.getElementById("rivenditoreAttributes").hidden = false;
    } else {
        document.getElementById("clienteAttributes").disabled = true;
        document.getElementById("clienteAttributes").hidden = true;
        document.getElementById("rivenditoreAttributes").disabled = true;
        document.getElementById("rivenditoreAttributes").hidden = true;
    }
}
const csrfToken = document.cookie.replace(/(?:(?:^|.*;\s*)XSRF-TOKEN\s*\=\s*([^;]*).*$)|^.*$/, '$1');

function clienteRegistration(){
    fetch("/api/cliente-registration", {
        method: "POST",
        headers: new Headers({
            "Contert-Type": "application/x-www-form-urlencoded",
            "X-XSRF-TOKEN": csrfToken
        }),
        body: new FormData(document.forms["registration"])
    })
    // return true
}

function rivenditoreRegistration(form){
    fetch("/api/rivenditore-registration", {
        method: "POST",
        headers: new Headers({
            "Contert-Type": "application/x-www-form-urlencoded",
            "X-XSRF-TOKEN": csrfToken
        }),
        body: new FormData(form)
    })
    // return true
}
