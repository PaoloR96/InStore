package com.howtodoinjava.app.applicationcore.entity;

public class ClientePremium extends Cliente {
        //definizione variabili
        protected String numeroCartaFedeltà;
        //costuttore
        public ClientePremium(String nome, String cognome, String email, String username, String password, String numCell, CartaCredito cartaCredito, Carrello carrello) {
            super(nome, cognome, email, username, password, numCell, cartaCredito, carrello);
        }
}
