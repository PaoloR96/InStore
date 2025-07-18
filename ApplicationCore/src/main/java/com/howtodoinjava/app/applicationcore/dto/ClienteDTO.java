package com.howtodoinjava.app.applicationcore.dto;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.springframework.web.util.HtmlUtils;

public class ClienteDTO extends Utente {
    @NotBlank(message = "Il nome non può essere vuoto.")
    @Size(max = 50, message = "Il nome non può superare i 50 caratteri.")
    private String nome;

    @NotBlank(message = "Il cognome non può essere vuoto.")
    @Size(max = 50, message = "Il cognome non può superare i 50 caratteri.")
    private String cognome;


    public ClienteDTO(Cliente cliente) {
        super(cliente.getUsername(), cliente.getEmail(), cliente.getNumCell());
        this.nome = cliente.getNome();
        this.cognome = cliente.getCognome();
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }


    public void escape(){
        this.username = HtmlUtils.htmlEscape(this.username);
        this.email = HtmlUtils.htmlEscape(this.email);
        this.numCell = HtmlUtils.htmlEscape(this.numCell);
        this.nome = HtmlUtils.htmlEscape(this.nome);
        this.cognome = HtmlUtils.htmlEscape(this.cognome);
    }
}
