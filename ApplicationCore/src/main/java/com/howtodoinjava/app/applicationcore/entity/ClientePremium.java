package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

        @Column(name = "numero_carta_fedelta", nullable = true)
        private String numeroCartaFedelta;
        @Column(nullable = true)
        private Integer sconto;

        public ClientePremium() {}

        public ClientePremium(String numeroCartaFedelta, Integer sconto) {
                this.numeroCartaFedelta = numeroCartaFedelta;
                this.sconto = sconto;
        }

        public ClientePremium(String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, List<Ordine> listaClienteOrdini, String numeroCartaFedelta, Integer sconto) {
                super(nome, cognome, cartaCredito, carrello, statoCliente, listaClienteOrdini);
                this.numeroCartaFedelta = numeroCartaFedelta;
                this.sconto = sconto;
        }

        public ClientePremium(String email, String username, String password, String numCell, String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, List<Ordine> listaClienteOrdini, String numeroCartaFedelta, Integer sconto) {
                super(email, username, password, numCell, nome, cognome, cartaCredito, carrello, statoCliente, listaClienteOrdini);
                this.numeroCartaFedelta = numeroCartaFedelta;
                this.sconto = sconto;
        }

        public String getNumeroCartaFedelta() {
                return numeroCartaFedelta;
        }

        public void setNumeroCartaFedelta(String numeroCartaFedelta) {
                this.numeroCartaFedelta = numeroCartaFedelta;
        }

        public Integer getSconto() {
                return sconto;
        }

        public void setSconto(Integer sconto) {
                this.sconto = sconto;
        }
}
