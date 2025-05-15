package com.howtodoinjava.app.applicationcore.dto;

import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import org.springframework.web.util.HtmlUtils;

public class RivenditoreDTO extends Utente {
    private String nomeSocieta;
    private String partitaIva;
    private String iban;

    public RivenditoreDTO(Rivenditore rivenditore) {
        super(
            rivenditore.getUsername(),
            rivenditore.getEmail(),
            rivenditore.getNumCell()
        );
        this.nomeSocieta = rivenditore.getNomeSocieta();
        this.partitaIva = rivenditore.getPartitaIva();
        this.iban = rivenditore.getIban();
    }

    public String getNomeSocieta() {
        return nomeSocieta;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public String getIban() {
        return iban;
    }

    public void escape(){
        this.username = HtmlUtils.htmlEscape(this.username);
        this.email = HtmlUtils.htmlEscape(this.email);
        this.numCell = HtmlUtils.htmlEscape(this.numCell);
        this.nomeSocieta = HtmlUtils.htmlEscape(this.nomeSocieta);
        this.partitaIva = HtmlUtils.htmlEscape(this.partitaIva);
        this.iban = HtmlUtils.htmlEscape(this.iban);
    }
}
