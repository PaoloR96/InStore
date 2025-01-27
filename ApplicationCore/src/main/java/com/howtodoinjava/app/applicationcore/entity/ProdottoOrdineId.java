package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProdottoOrdineId implements Serializable {

    @Column(name = "id_prodotto", nullable = false)
    private Long idProdotto;

    @Column(name = "id_ordine", nullable = false)
    private Long idOrdine;

    public ProdottoOrdineId() {}

    public ProdottoOrdineId(Long idProdotto, Long idOrdine) {
        this.idProdotto = idProdotto;
        this.idOrdine = idOrdine;
    }

    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }

    public Long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Long idOrdine) {
        this.idOrdine = idOrdine;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProdottoOrdineId that = (ProdottoOrdineId) o;
        return Objects.equals(idProdotto, that.idProdotto) && Objects.equals(idOrdine, that.idOrdine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProdotto, idOrdine);
    }
}

