package com.howtodoinjava.app.applicationcore.entity;

import java.awt.image.BufferedImage;

public class Prodotto {
            //definizione variabili
            protected Long idProdotto;
            protected String nomeProdotto;
            protected String descrizione;
            protected Float prezzo;
            protected String taglia;
            protected BufferedImage immagine;
            protected String pathImmagine;
            protected Integer quantitaTotale;
            protected Rivenditore rivenditore;
            //costruttore
            public Prodotto(Long idProdotto, String nomeProdotto, String descrizione, Float prezzo, String taglia, BufferedImage immagine, String pathImmagine, Integer quantitaTotale, Rivenditore rivenditore) {
                this.idProdotto = idProdotto;
                this.nomeProdotto = nomeProdotto;
                this.descrizione = descrizione;
                this.prezzo = prezzo;
                this.taglia = taglia;
                this.immagine = immagine;
                this.pathImmagine = pathImmagine;
                this.quantitaTotale = quantitaTotale;
                this.rivenditore = rivenditore;
            }
            //Get&Set
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

            public String getTaglia() {
                return taglia;
            }

            public void setTaglia(String taglia) {
                this.taglia = taglia;
            }

            public BufferedImage getImmagine() {
                return immagine;
            }

            public void setImmagine(BufferedImage immagine) {
                this.immagine = immagine;
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

            public Rivenditore getRivenditore() {
                return rivenditore;
            }

            public void setRivenditore(Rivenditore rivenditore) {
                this.rivenditore = rivenditore;
            }
}
