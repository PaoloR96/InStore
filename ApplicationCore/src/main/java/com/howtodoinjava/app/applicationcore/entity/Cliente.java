package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;


//TODO CLASSE DTO E MAPPER

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name="CLIENTE")
public class Cliente extends  Utente {
        //definizione variabili
        private String nome;
        private String cognome;
        @OneToOne
        private CartaCredito cartaCredito;
        @OneToOne
        private Carrello carrello;
        @Enumerated(EnumType.STRING)
        private StatoCliente statoCliente;

        public Cliente() {}

        public Cliente(String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente) {
                this.nome = nome;
                this.cognome = cognome;
                this.cartaCredito = cartaCredito;
                this.carrello = carrello;
                this.statoCliente = statoCliente;
        }

        public Cliente(String email, String username, String password, String numCell, String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente) {
                super(email, username, password, numCell);
                this.nome = nome;
                this.cognome = cognome;
                this.cartaCredito = cartaCredito;
                this.carrello = carrello;
                this.statoCliente = statoCliente;
        }

        public String getNome() {
                return nome;
        }

        public void setNome(String nome) {
                this.nome = nome;
        }

        public String getCognome() {
                return cognome;
        }

        public void setCognome(String cognome) {
                this.cognome = cognome;
        }

        public CartaCredito getCartaCredito() {
                return cartaCredito;
        }

        public void setCartaCredito(CartaCredito cartaCredito) {
                this.cartaCredito = cartaCredito;
        }

        public Carrello getCarrello() {
                return carrello;
        }

        public void setCarrello(Carrello carrello) {
                this.carrello = carrello;
        }

        public StatoCliente getStatoCliente() {
                return statoCliente;
        }

        public void setStatoCliente(StatoCliente statoCliente) {
                this.statoCliente = statoCliente;
        }
}
