async function upgradeToPremium() {
    try {
        // let csrf_token = $("meta[name='_csrf']").attr("content");
        // let csrf_header = $("meta[name='_csrf_header']").attr("content");
        //
        // const response = await fetch(`${API_BASE_URL}/upgrade`, {
        //     method: 'PUT',
        //     headers: {
        //         [csrf_header] : csrf_token,
        //     }
        // });

        const response = await sendRequest(`${API_BASE_URL}/upgrade`, 'PUT')

        if (response.ok) {
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
