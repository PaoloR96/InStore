package com.howtodoinjava.app.applicationcore.dto;

public class ProdottoDTO {
    private Long idProdotto;
    private String nomeProdotto;
    private String descrizione;
    private Float prezzo;
    private String taglia;
    private String immagine;
    private String pathImmagine;
    private Integer quantitaTotale;

    public ProdottoDTO() {}

    public ProdottoDTO(Long idProdotto, String nomeProdotto, String descrizione, Float prezzo, String taglia, String immagine, String pathImmagine, Integer quantitaTotale) {
        this.idProdotto = idProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.taglia = taglia;
        this.immagine = immagine;
        this.pathImmagine = pathImmagine;
        this.quantitaTotale = quantitaTotale;
    }

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

    public String getImmagine() {
        return immagine;
    }

    public void setImmagine(String immagine) {
        this.immagine = immagine;
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
}
