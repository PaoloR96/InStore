package com.howtodoinjava.app.applicationcore.entity;

//import com.fasterxml.jackson.annotation.JsonIgnore; // TODO remove
import jakarta.persistence.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="RIVENDITORE")
public class Rivenditore  extends Utente {

    @Column(name = "nome_societa", nullable = false)
    private String nomeSocieta;
    @Column(name = "partita_iva", nullable = false, unique = true)
    private String partitaIva;
    @Column(nullable = false, unique = true)
    private String iban;
    @OneToMany(mappedBy = "rivenditore", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Prodotto> listaProdottiRivenditore;

    // TODO remove
//    @Column(name = "stato_rivenditore", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private StatoRivenditore statoRivenditore;

    public Rivenditore(){}

//    public Rivenditore(String nomeSocieta, String partitaIva, String iban, List<Prodotto> listaProdottiRivenditore, StatoRivenditore statoRivenditore) {
//        this.nomeSocieta = nomeSocieta;
//        this.partitaIva = partitaIva;
//        this.iban = iban;
//        this.listaProdottiRivenditore = listaProdottiRivenditore;
//        this.statoRivenditore = statoRivenditore;
//    }

    public Rivenditore(String email,
                       String username,
                       String numCell,
//                       boolean status,
                       String nomeSocieta,
                       String partitaIva,
                       String iban,
                       List<Prodotto> listaProdottiRivenditore
//                       StatoRivenditore statoRivenditore
    ) {
        super(username, email, numCell);
//        super(username, email, numCell, status);
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.listaProdottiRivenditore = listaProdottiRivenditore;
//        this.statoRivenditore = statoRivenditore;
    }

    public Rivenditore(String email,
                       String username,
                       String numCell,
//                       boolean status,
                       String nomeSocieta,
                       String partitaIva,
                       String iban
    ) {
        super(username, email, numCell);
//        super(username, email, numCell, status);
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.listaProdottiRivenditore = new ArrayList<Prodotto>();
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

    //TODO remove
//    public StatoRivenditore getStatoRivenditore() {
//        return statoRivenditore;
//    }
//
//    public void setStatoRivenditore(StatoRivenditore statoRivenditore) {
//        this.statoRivenditore = statoRivenditore;
//    }
}
