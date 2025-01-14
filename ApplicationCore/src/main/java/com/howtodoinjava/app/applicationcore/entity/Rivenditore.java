package com.howtodoinjava.app.applicationcore.entity;

import java.util.ArrayList;

public class Rivenditore {
            //definizione variabili
            protected String nomeSocieta;
            protected String partitaIva;
            protected String email;
            protected String username;
            protected String password;
            protected String numeroTelefono;
            protected String iban;
            protected ArrayList <Prodotto> listaProdottiRivenditore;
            //costruttore
            public Rivenditore(String nomeSocieta, String partitaIva, String email, String username, String password, String numeroTelefono, String iban, ArrayList<Prodotto> listaProdottiRivenditore) {
                this.nomeSocieta = nomeSocieta;
                this.partitaIva = partitaIva;
                this.email = email;
                this.username = username;
                this.password = password;
                this.numeroTelefono = numeroTelefono;
                this.iban = iban;
                this.listaProdottiRivenditore = listaProdottiRivenditore;
            }
            //Get&Set
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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getNumeroTelefono() {
                return numeroTelefono;
            }

            public void setNumeroTelefono(String numeroTelefono) {
                this.numeroTelefono = numeroTelefono;
            }

            public String getIban() {
                return iban;
            }

            public void setIban(String iban) {
                this.iban = iban;
            }

            public ArrayList<Prodotto> getListaProdottiRivenditore() {
                return listaProdottiRivenditore;
            }

            public void setListaProdottiRivenditore(ArrayList<Prodotto> listaProdottiRivenditore) {
                this.listaProdottiRivenditore = listaProdottiRivenditore;
            }
}
