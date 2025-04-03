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