package com.howtodoinjava.app.applicationcore.entity;
import jakarta.persistence.*;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="QUANTITA_PRODOTTO_ORDINE")
public class QuantitaPrdottoOrdine {
    //definzione variabili
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Prodotto prodotto;
    @OneToOne
    private Ordine ordine;
    private int quantita;

    public QuantitaPrdottoOrdine() {}

    public QuantitaPrdottoOrdine(Long id, Prodotto prodotto, Ordine ordine, int quantita) {
        this.id = id;
        this.prodotto = prodotto;
        this.ordine = ordine;
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

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }
}
