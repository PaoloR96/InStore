async function upgradeToPremium() {
    try {
        const response = await fetch(`${API_BASE_URL}/upgrade`, {
            method: 'PUT'
        });

        if (response.ok) {
            //document.getElementById('premiumBadge').classList.add('active');
            alert('Congratulazioni! Ora sei un cliente PREMIUM!');
        } else {
            if(response.status === 400) {
                const error = await response.text();
                alert(error || 'Cliente già premium');
            } else {
                const error = await response.text();
                alert(error || 'Errore');
            }
        }
    } catch (error) {
        console.error('Errore nell\'upgrade a premium:', error);
        alert('Si è verificato un errore. Riprova più tardi.');
    }
}
