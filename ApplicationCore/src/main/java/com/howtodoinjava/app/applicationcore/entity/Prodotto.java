package com.howtodoinjava.app.applicationcore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="PRODOTTO")
public class Prodotto {
            //definizione variabili
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prodotto")
    private Long idProdotto;

    @Column(name = "nome_prodotto", nullable = false, length = 100)
    private String nomeProdotto;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String descrizione;

    @Column(nullable = false)
    private Float prezzo;


    @Column(name = "path_immagine")
    private String pathImmagine;

    @Column(name = "quantita_totale", nullable = false)
    private Integer quantitaTotale;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Taglia taglia;

    @ManyToOne
    @JoinColumn(name = "rivenditore_username", nullable = false)
    @JsonIgnore
    private Rivenditore rivenditore;

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProdottoCarrello> listaProdottiCarrello;

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ProdottoOrdine> listaProdottiOrdine;

    public Prodotto() {}

    public Prodotto(Long idProdotto, String nomeProdotto, String descrizione, Float prezzo, String pathImmagine, Integer quantitaTotale, Taglia taglia, Rivenditore rivenditore, List<ProdottoCarrello> listaProdottiCarrello, List<ProdottoOrdine> listaProdottiOrdine) {
        this.idProdotto = idProdotto;
        this.nomeProdotto = nomeProdotto;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.pathImmagine = pathImmagine;
        this.quantitaTotale = quantitaTotale;
        this.taglia = taglia;
        this.rivenditore = rivenditore;
        this.listaProdottiCarrello = listaProdottiCarrello;
        this.listaProdottiOrdine = listaProdottiOrdine;
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

    public Taglia getTaglia() {
        return taglia;
    }

    public void setTaglia(Taglia taglia) {
        this.taglia = taglia;
    }

    public Rivenditore getRivenditore() {
        return rivenditore;
    }

    public void setRivenditore(Rivenditore rivenditore) {
        this.rivenditore = rivenditore;
    }

    public List<ProdottoCarrello> getListaProdottiCarrello() {
        return listaProdottiCarrello;
    }

    public void setListaProdottiCarrello(List<ProdottoCarrello> listaProdottiCarrello) {
        this.listaProdottiCarrello = listaProdottiCarrello;
    }

    public List<ProdottoOrdine> getListaProdottiOrdine() {
        return listaProdottiOrdine;
    }

    public void setListaProdottiOrdine(List<ProdottoOrdine> listaProdottiOrdine) {
        this.listaProdottiOrdine = listaProdottiOrdine;
    }

    public void escape(){
        this.descrizione = HtmlUtils.htmlEscape(this.descrizione);
        this.nomeProdotto = HtmlUtils.htmlEscape(this.nomeProdotto);
    }
}
