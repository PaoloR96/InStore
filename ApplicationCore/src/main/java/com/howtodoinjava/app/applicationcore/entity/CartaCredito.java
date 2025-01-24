package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Date;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARTA_DI_CREDITO")
public class CartaCredito {
        //definzione variabili
        @Id
        private String numeroCarta;
        private Date dataScadenza;
        private String nomeIntestatario;
        private String cognomeIntestatario;
        private Integer cvc;
        @OneToOne
        private Cliente cliente;

        public CartaCredito() {}

        public CartaCredito(String numeroCarta, Date dataScadenza, String nomeIntestatario, String cognomeIntestatario, Integer cvc, Cliente cliente) {
                this.numeroCarta = numeroCarta;
                this.dataScadenza = dataScadenza;
                this.nomeIntestatario = nomeIntestatario;
                this.cognomeIntestatario = cognomeIntestatario;
                this.cvc = cvc;
                this.cliente = cliente;
        }

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