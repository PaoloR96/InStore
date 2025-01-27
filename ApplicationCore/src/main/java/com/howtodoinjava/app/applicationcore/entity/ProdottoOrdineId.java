package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

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
}

