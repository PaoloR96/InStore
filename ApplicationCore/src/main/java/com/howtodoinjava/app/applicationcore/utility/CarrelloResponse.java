package com.howtodoinjava.app.applicationcore.utility;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;

import java.util.List;

public class CarrelloResponse {
    private Float prezzoTotale;
    private List<Prodotto> prodotti;
    private Integer scontoApplicato;

    public CarrelloResponse(Float prezzoTotale, List<Prodotto> prodotti) {
        this.prezzoTotale = prezzoTotale;
        this.prodotti = prodotti;
    }

    public CarrelloResponse() {

    }

    public void setPrezzoTotale(Float prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    public Integer getScontoApplicato() {
        return scontoApplicato;
    }

    public void setScontoApplicato(Integer scontoApplicato) {
        this.scontoApplicato = scontoApplicato;
    }

    public Float getPrezzoTotale() {
        return prezzoTotale;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void escape(){
        prodotti.forEach(Prodotto::escape);
    }
}
