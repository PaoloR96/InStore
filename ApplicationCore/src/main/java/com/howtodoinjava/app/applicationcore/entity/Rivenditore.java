package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="RIVENDITORE")
public class Rivenditore  extends Utente {

    @Column(name = "nome_societa", nullable = false)
    @NotBlank(message = "Il nome della società non può essere vuoto.")
    @Size(max = 100, message = "Il nome della società non può superare i 100 caratteri.")
    private String nomeSocieta;

    @Column(name = "partita_iva", nullable = false, unique = true)
    @NotBlank(message = "La Partita IVA non può essere vuota.")
    @Pattern(regexp = "^[0-9]{11}$", message = "La Partita IVA deve essere di 11 cifre numeriche.")
    private String partitaIva;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "L'IBAN non può essere vuoto.")
    @Pattern(
            regexp = "^IT\\d{2}[A-Z]\\d{5}\\d{5}\\d{12}$",
            message = "L'IBAN non è in un formato valido (es. IT60X0542811101000000123456)."
    )    private String iban;

    @OneToMany(mappedBy = "rivenditore", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    @JsonIgnore
    private List<Prodotto> listaProdottiRivenditore;

    public Rivenditore() {
    }

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
                       String nomeSocieta,
                       String partitaIva,
                       String iban,
                       List<Prodotto> listaProdottiRivenditore
    ) {
        super(username, email, numCell);
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.listaProdottiRivenditore = listaProdottiRivenditore;
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
}