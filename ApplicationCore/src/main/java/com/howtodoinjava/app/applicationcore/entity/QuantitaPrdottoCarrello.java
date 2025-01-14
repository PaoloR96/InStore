package com.howtodoinjava.app.applicationcore.entity;

public class QuantitaPrdottoCarrello {
            //definzione variabili
            protected Prodotto prodotto;
            protected Carrello carrello;
            protected int quantita;
            //costruttore
            public QuantitaPrdottoCarrello(Prodotto prodotto, Carrello carrello, int quantita) {
                    this.prodotto = prodotto;
                    this.carrello = carrello;
                    this.quantita = quantita;

            }
            //Get&Set
            public Prodotto getProdotto() {
                return prodotto;
            }

            public void setProdotto(Prodotto prodotto) {
                this.prodotto = prodotto;
            }

            public Carrello getCarrello() {
                return carrello;
            }

            public void setCarrello(Carrello carrello) {
                this.carrello = carrello;
            }

            public int getQuantita() {
                return quantita;
            }

            public void setQuantita(int quantita) {
                this.quantita = quantita;
            }
}
