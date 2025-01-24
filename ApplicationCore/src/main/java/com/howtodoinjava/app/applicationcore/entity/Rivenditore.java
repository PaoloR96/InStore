package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="RIVENDITORE")
public class Rivenditore  extends  Utente{
    //definizione variabili
    private String nomeSocieta;
    private String partitaIva;
    private String iban;
    @OneToMany(mappedBy = "rivenditore", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prodotto> listaProdottiRivenditore;
    @Enumerated(EnumType.STRING)
    private StatoRivenditore statoRivenditore;

    public Rivenditore(){}

    public Rivenditore(String nomeSocieta, String partitaIva, String iban, List<Prodotto> listaProdottiRivenditore, StatoRivenditore statoRivenditore) {
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.listaProdottiRivenditore = listaProdottiRivenditore;
        this.statoRivenditore = statoRivenditore;
    }

    public String getNomeSocieta() {
        return nomeSocieta;
    }

    public void setNomeSocieta(String nomeSocieta) {
        this.nomeSocieta = nomeSocieta;
    }

    public String getPartitaIva() {
        return partitaIva;
    }

    public void setPartitaIva(String partitaIva) {
        this.partitaIva = partitaIva;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public List<Prodotto> getListaProdottiRivenditore() {
        return listaProdottiRivenditore;
    }

    public void setListaProdottiRivenditore(List<Prodotto> listaProdottiRivenditore) {
        this.listaProdottiRivenditore = listaProdottiRivenditore;
    }

    public StatoRivenditore getStatoRivenditore() {
        return statoRivenditore;
    }

    public void setStatoRivenditore(StatoRivenditore statoRivenditore) {
        this.statoRivenditore = statoRivenditore;
    }

    public Rivenditore(String email, String username, String password, String numCell, String nomeSocieta, String partitaIva, String iban, List<Prodotto> listaProdottiRivenditore, StatoRivenditore statoRivenditore) {
        super(email, username, password, numCell);
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.listaProdottiRivenditore = listaProdottiRivenditore;
        this.statoRivenditore = statoRivenditore;
    }
}
