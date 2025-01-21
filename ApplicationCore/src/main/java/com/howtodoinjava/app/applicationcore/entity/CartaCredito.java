package com.howtodoinjava.app.applicationcore.entity;

import java.util.Date;

public class CartaCredito {
        //definzione variabili
        protected String numeroCarta;
        protected Date dataScadenza;
        protected String nomeIntestatario;
        protected String cognomeIntestatario;
        private Integer cvc;

        public CartaCredito(String numeroCarta, Date dataScadenza, String nomeIntestatario, String cognomeIntestatario, Integer cvc) {
                this.numeroCarta = numeroCarta;
                this.dataScadenza = dataScadenza;
                this.nomeIntestatario = nomeIntestatario;
                this.cognomeIntestatario = cognomeIntestatario;
                this.cvc = cvc;
        }
        //Get&Set
        public String getNumeroCarta() {
                return numeroCarta;
        }

        public void setNumeroCarta(String numeroCarta) {
                this.numeroCarta = numeroCarta;
        }

        public Date getDataScadenza() {
                return dataScadenza;
        }

        public void setDataScadenza(Date dataScadenza) {
                this.dataScadenza = dataScadenza;
        }

        public String getNomeIntestatario() {
                return nomeIntestatario;
        }

        public void setNomeIntestatario(String nomeIntestatario) {
                this.nomeIntestatario = nomeIntestatario;
        }

        public String getCognomeIntestatario() {
                return cognomeIntestatario;
        }

        public void setCognomeIntestatario(String cognomeIntestatario) {
                this.cognomeIntestatario = cognomeIntestatario;
        }

        public Integer getCvc() {
                return cvc;
        }

        public void setCvc(Integer cvc) {
                this.cvc = cvc;
        }
}
