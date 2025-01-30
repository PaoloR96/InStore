package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

        @Column
        private Integer sconto;

        public ClientePremium() {}

        public ClientePremium(Integer sconto) {
                this.sconto = sconto;
        }

        public ClientePremium(String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, List<Ordine> listaClienteOrdini, String numeroCartaFedelta, Integer sconto) {
                super(nome, cognome, cartaCredito, carrello, statoCliente, listaClienteOrdini);
                this.sconto = sconto;
        }

        public Integer getSconto() {
                return sconto;
        }

        public void setSconto(Integer sconto) {
                this.sconto = sconto;
        }
}
