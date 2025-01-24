package com.howtodoinjava.app.applicationcore.dto;

import java.util.List;

public class RivenditoreDTO {
    private String nomeSocieta;
    private String partitaIva;
    private String iban;
    private String statoRivenditore;
    private List<ProdottoDTO> listaProdottiRivenditore;

    public RivenditoreDTO() {}

    public RivenditoreDTO(String nomeSocieta, String partitaIva, String iban, String statoRivenditore, List<ProdottoDTO> listaProdottiRivenditore) {
        this.nomeSocieta = nomeSocieta;
        this.partitaIva = partitaIva;
        this.iban = iban;
        this.statoRivenditore = statoRivenditore;
        this.listaProdottiRivenditore = listaProdottiRivenditore;
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

    public String getStatoRivenditore() {
        return statoRivenditore;
    }

    public void setStatoRivenditore(String statoRivenditore) {
        this.statoRivenditore = statoRivenditore;
    }

    public List<ProdottoDTO> getListaProdottiRivenditore() {
        return listaProdottiRivenditore;
    }

    public void setListaProdottiRivenditore(List<ProdottoDTO> listaProdottiRivenditore) {
        this.listaProdottiRivenditore = listaProdottiRivenditore;
    }
}
