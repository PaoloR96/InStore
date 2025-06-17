package com.howtodoinjava.app.applicationcore.entity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="PRODOTTO_ORDINE")
public class ProdottoOrdine {

    @EmbeddedId
    private ProdottoOrdineId id;

    @ManyToOne
    @MapsId("idOrdine")
    @NotNull(message = "L'ordine non può essere nullo.")
    @Valid
    @JoinColumn(name = "id_ordine", nullable = false)
    private Ordine ordine;

    @ManyToOne
    @MapsId("idProdotto")
    @NotNull(message = "Il prodotto non può essere nullo.")
    @Valid
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto prodotto;

    @Column(nullable = false)
    @NotNull(message = "La quantità del prodotto nell'ordine non può essere nulla.")
    @Min(value = 1, message = "La quantità del prodotto nell'ordine deve essere almeno 1.")
    private Integer quantita;

    public ProdottoOrdine() {}

    public ProdottoOrdine(ProdottoOrdineId id, Ordine ordine, Prodotto prodotto, Integer quantita) {
        this.id = id;
        this.ordine = ordine;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public ProdottoOrdineId getId() {
        return id;
    }

    public void setId(ProdottoOrdineId id) {
        this.id = id;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
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
