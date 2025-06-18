package com.howtodoinjava.app.applicationcore.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import org.springframework.web.util.HtmlUtils;

import java.io.Serializable;

public class ProdottoDTO implements Serializable {
    private Long idProdotto;

    @NotBlank(message = "Il nome del prodotto non può essere vuoto.")
    @Size(max = 30, message = "Il nome del prodotto non può superare i 30 caratteri.")
    private String nomeProdotto;

    @Size(max = 200, message = "La descrizione non può superare i 200 caratteri.")
    private String descrizione;

    @NotNull(message = "Il prezzo non può essere nullo.")
    @DecimalMin(value = "0.0", message = "Il prezzo non può essere negativo.") // Usato DecimalMin per float
    private Float prezzo;

    @NotBlank(message = "La taglia non può essere vuota.") // Se Taglia è un enum convertito in String
    private String taglia;

    private String pathImmagine;

    @NotNull(message = "La quantità totale non può essere nulla.")
    @Min(value = 0, message = "La quantità totale non può essere negativa.")
    private Integer quantitaTotale;


    // Costruttore con parametri
    public ProdottoDTO() {
        this.idProdotto = idProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.taglia = taglia;
        this.pathImmagine = pathImmagine;
        this.quantitaTotale = quantitaTotale;
    }

    // Getters e Setters
    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNomeProdotto() {
        return nomeProdotto;
    }

    public void setNomeProdotto(String nomeProdotto) {
        this.nomeProdotto = nomeProdotto;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public String getTaglia() {
        return taglia;
    }

    public void setTaglia(String taglia) {
        this.taglia = taglia;
    }


    public String getPathImmagine() {
        return pathImmagine;
    }

    public void setPathImmagine(String pathImmagine) {
        this.pathImmagine = pathImmagine;
    }

    public Integer getQuantitaTotale() {
        return quantitaTotale;
    }

    public void setQuantitaTotale(Integer quantitaTotale) {
        this.quantitaTotale = quantitaTotale;
    }

    public void escape(){
        this.descrizione = HtmlUtils.htmlEscape(this.descrizione);
        this.nomeProdotto = HtmlUtils.htmlEscape(this.nomeProdotto);
        this.taglia = HtmlUtils.htmlEscape(this.taglia);
    }
}
