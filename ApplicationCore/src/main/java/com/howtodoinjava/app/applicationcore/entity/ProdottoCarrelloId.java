package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ProdottoCarrelloId implements Serializable {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "id_prodotto", nullable = false)
    private Long idProdotto;

    public ProdottoCarrelloId() {}

    public ProdottoCarrelloId(String username, Long idProdotto) {
        this.username = username;
        this.idProdotto = idProdotto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Long idProdotto) {
        this.idProdotto = idProdotto;
    }
}
