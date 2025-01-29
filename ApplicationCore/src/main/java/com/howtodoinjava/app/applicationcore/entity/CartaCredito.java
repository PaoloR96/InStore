package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;

import java.util.Date;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARTA_DI_CREDITO")
public class CartaCredito {

        @Id
        @Column(name = "numero_carta")
        private String numeroCarta;
        @Column(name = "data_sacdenza", nullable = false)
        @Temporal(TemporalType.DATE)
        private Date dataScadenza;
        @Column(name = "nome_intestatario", nullable = false)
        private String nomeIntestatario;
        @Column(name = "cognome_intestatario", nullable = false)
        private String cognomeIntestatario;
        @Column(nullable = false, columnDefinition = "NUMERIC(3, 0)")
        @Digits(integer = 3, fraction = 0)
        private Integer cvc;

        @OneToOne(mappedBy = "cartaCredito", orphanRemoval = true)
        private Cliente cliente;

        public CartaCredito() {
        }

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

        public Cliente getCliente() {
                return cliente;
        }

        public void setCliente(Cliente cliente) {
                this.cliente = cliente;
        }
}