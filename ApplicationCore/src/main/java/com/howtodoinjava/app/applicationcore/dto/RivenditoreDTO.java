package com.howtodoinjava.app.applicationcore.dto;

import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import com.howtodoinjava.app.applicationcore.entity.Utente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.util.HtmlUtils;

public class RivenditoreDTO extends Utente {

    @NotBlank(message = "Il nome della società non può essere vuoto.")
    @Size(max = 100, message = "Il nome della società non può superare i 100 caratteri.")
    private String nomeSocieta;

    @NotBlank(message = "La Partita IVA non può essere vuota.")
    @Pattern(regexp = "^[0-9]{11}$", message = "La Partita IVA deve essere di 11 cifre numeriche.")
    private String partitaIva;

    @NotBlank(message = "L'IBAN non può essere vuoto.")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{4,30}$", message = "L'IBAN non è in un formato valido.")
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
