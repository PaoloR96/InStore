package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

import java.security.PublicKey;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="QUANTITA_PRODOTTO_CARRELLO")
public class QuantitaPrdottoCarrello {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Prodotto prodotto;
    @OneToOne
    private Carrello carrello;
    private Integer quantita;

    public QuantitaPrdottoCarrello() {}

    public QuantitaPrdottoCarrello(Long id, Prodotto prodotto, Carrello carrello, Integer quantita) {
        this.id = id;
        this.prodotto = prodotto;
        this.carrello = carrello;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Carrello getCarrello() {
        return carrello;
    }

    public void setCarrello(Carrello carrello) {
        this.carrello = carrello;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}
