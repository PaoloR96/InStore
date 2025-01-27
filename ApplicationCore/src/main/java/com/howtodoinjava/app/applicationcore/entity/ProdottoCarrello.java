package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="PRODOTTO_CARRELLO")
public class ProdottoCarrello {

    @EmbeddedId
    private ProdottoCarrelloId id;


    @ManyToOne
    @MapsId("username") // Mappa il campo idCarrello nella chiave composta
    @JoinColumn(name = "username", nullable = false)
    private Carrello carrello;

    @ManyToOne
    @MapsId("idProdotto") // Mappa il campo idProdotto nella chiave composta
    @JoinColumn(name = "id_Prodotto", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    private Integer quantita;

    public ProdottoCarrello() {}

    public ProdottoCarrello(ProdottoCarrelloId id, Carrello carrello, Prodotto prodotto, Integer quantita) {
        this.id = id;
        this.carrello = carrello;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public ProdottoCarrelloId getId() {
        return id;
    }

    public void setId(ProdottoCarrelloId id) {
        this.id = id;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

}
