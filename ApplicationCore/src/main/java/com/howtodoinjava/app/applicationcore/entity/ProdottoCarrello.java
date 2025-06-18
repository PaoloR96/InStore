package com.howtodoinjava.app.applicationcore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="PRODOTTO_CARRELLO")
public class ProdottoCarrello {

    @EmbeddedId
    private ProdottoCarrelloId id;


    @ManyToOne
    @MapsId("username") // Mappa il campo idCarrello nella chiave composta
    @JoinColumn(name = "username", nullable = false)
    @NotNull(message = "Il carrello non può essere nullo per ProdottoCarrello.")
    @Valid
    @JsonIgnore
    private Carrello carrello;

    @ManyToOne
    @MapsId("idProdotto") // Mappa il campo idProdotto nella chiave composta
    @JoinColumn(name = "id_Prodotto", nullable = false)
    @NotNull(message = "Il prodotto non può essere nullo per ProdottoCarrello.")
    @Valid
    @JsonIgnore
    private Prodotto prodotto;

    @Column(nullable = false)
    @NotNull(message = "La quantità nel carrello non può essere nulla.")
    @Min(value = 1, message = "La quantità nel carrello deve essere almeno 1.")
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

    public void escape(){
        this.prodotto.escape();
    }

}
