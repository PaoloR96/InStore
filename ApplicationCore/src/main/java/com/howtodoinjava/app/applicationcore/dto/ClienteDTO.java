package com.howtodoinjava.app.applicationcore.dto;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import org.springframework.web.util.HtmlUtils;

public class ClienteDTO extends Utente {
    private String nome;
    private String cognome;
    private String numeroCarta;

    public ClienteDTO(Cliente cliente) {
        super(cliente.getUsername(), cliente.getEmail(), cliente.getNumCell());
        this.nome = cliente.getNome();
        this.cognome = cliente.getCognome();
        this.numeroCarta = cliente.getCartaCredito().getNumeroCarta();
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getNumeroCarta() {
        return numeroCarta;
    }

    public void escape(){
        this.username = HtmlUtils.htmlEscape(this.username);
        this.email = HtmlUtils.htmlEscape(this.email);
        this.numCell = HtmlUtils.htmlEscape(this.numCell);
        this.nome = HtmlUtils.htmlEscape(this.nome);
        this.cognome = HtmlUtils.htmlEscape(this.cognome);
        this.numeroCarta = HtmlUtils.htmlEscape(this.numeroCarta);
    }
}
