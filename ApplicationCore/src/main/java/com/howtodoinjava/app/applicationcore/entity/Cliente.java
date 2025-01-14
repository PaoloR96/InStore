package com.howtodoinjava.app.applicationcore.entity;

public class Cliente {
        //definizione variabili
        protected String nome;
        protected String cognome;
        protected String email;
        protected String username;
        protected String password ;
        protected String numCell;
        protected CartaCredito cartaCredito;
        protected Carrello carrello;
        //costruttore
        public Cliente(String nome, String cognome, String email, String username, String password, String numCell, CartaCredito cartaCredito, Carrello carrello) {
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
            this.username = username;
            this.password = password;
            this.numCell = numCell;
            this.cartaCredito = cartaCredito;
            this.carrello = carrello;
        }
        //Get&Set
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

        public String getNumCell() {
            return numCell;
        }

        public void setNumCell(String numCell) {
            this.numCell = numCell;
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
}
