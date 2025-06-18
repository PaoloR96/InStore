package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@DiscriminatorValue("PREMIUM")
public class ClientePremium extends Cliente {

        @Column
        @Min(value = 0, message = "Lo sconto non può essere negativo.")
        @Max(value = 100, message = "Lo sconto non può superare il 100%.")
        private Integer sconto;

        public ClientePremium() {}

        public ClientePremium(Integer sconto) {
                this.sconto = sconto;
        }

        public ClientePremium(String nome, String cognome, CartaCredito cartaCredito, Carrello carrello, StatoCliente statoCliente, List<Ordine> listaClienteOrdini, String numeroCartaFedelta, Integer sconto) {
                super(nome, cognome, cartaCredito, carrello, listaClienteOrdini);
                this.sconto = sconto;
        }

        public Integer getSconto() {
                return sconto;
        }

        public void setSconto(Integer sconto) {
                this.sconto = sconto;
        }
}
