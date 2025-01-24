package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Entity;

import java.util.ArrayList;
//TODO CLASSE DTO E MAPPER

@Entity
public class ClientePremium extends Cliente {
        //definizione variabili
        private String numeroCartaFedelta;

        public ClientePremium() {}
        public ClientePremium(String numeroCartaFedelta) {
                this.numeroCartaFedelta = numeroCartaFedelta;
        }

        public ClientePremium(String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, String numeroCartaFedelta) {
                super(nome, cognome, cartaCredito, carrello, statoCliente);
                this.numeroCartaFedelta = numeroCartaFedelta;
        }

        public ClientePremium(String email, String username, String password, String numCell, String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, String numeroCartaFedelta) {
                super(email, username, password, numCell, nome, cognome, cartaCredito, carrello, statoCliente);
                this.numeroCartaFedelta = numeroCartaFedelta;
        }

        public String getNumeroCartaFedelta() {
                return numeroCartaFedelta;
        }

        public void setNumeroCartaFedelta(String numeroCartaFedelta) {
                this.numeroCartaFedelta = numeroCartaFedelta;
        }
}
