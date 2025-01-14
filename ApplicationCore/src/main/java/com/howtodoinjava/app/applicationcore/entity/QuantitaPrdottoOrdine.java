package com.howtodoinjava.app.applicationcore.entity;

public class QuantitaPrdottoOrdine {
            //definzione variabili
            protected Prodotto prodotto;
            protected Ordine ordine;
            protected int quantita;
            //costruttore
            public QuantitaPrdottoOrdine(Prodotto prodotto, Ordine ordine, int quantita) {
                this.prodotto = prodotto;
                this.ordine = ordine;
                this.quantita = quantita;
            }
        //Get&Set
        public Prodotto getProdotto() {
            return prodotto;
        }

        public void setProdotto(Prodotto prodotto) {
            this.prodotto = prodotto;
        }

        public Ordine getOrdine() {
            return ordine;
        }

        public void setOrdine(Ordine ordine) {
            this.ordine = ordine;
        }

        public int getQuantita() {
            return quantita;
        }

        public void setQuantita(int quantita) {
            this.quantita = quantita;
        }
}
