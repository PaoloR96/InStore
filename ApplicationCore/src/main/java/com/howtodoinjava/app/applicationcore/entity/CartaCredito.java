package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.util.Date;

//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARTA_DI_CREDITO")
public class CartaCredito {

        @Id
        @Column(name = "numero_carta")
        @NotBlank(message = "Il numero della carta non può essere vuoto.")
        @CreditCardNumber(message = "Il numero della carta di credito non è valido.")
        private String numeroCarta;

        @Column(name = "data_scadenza", nullable = false)
        @Temporal(TemporalType.DATE)
        @NotNull(message = "La data di scadenza non può essere nulla.")
        @Future(message = "La data di scadenza deve essere nel futuro.")
        private Date dataScadenza;

        @Column(name = "nome_intestatario", nullable = false)
        @NotBlank(message = "Il nome dell'intestatario non può essere vuoto.")
        @Size(max = 100, message = "Il nome dell'intestatario non può superare i 100 caratteri.")
        private String nomeIntestatario;

        @Column(name = "cognome_intestatario", nullable = false)
        @NotBlank(message = "Il cognome dell'intestatario non può essere vuoto.")
        @Size(max = 100, message = "Il cognome dell'intestatario non può superare i 100 caratteri.")
        private String cognomeIntestatario;

        @Column(nullable = false, columnDefinition = "NUMERIC(3, 0)")
        @NotBlank(message = "Il CVC non può essere vuoto.")
        @Size(min = 3, max = 3, message = "Il CVC deve avere 3 cifre.") // Permetti 3 o 4 (per Amex)
        @Pattern(regexp = "^[0-9]{3}$", message = "Il CVC deve contenere solo numeri.")
        private String cvc;

        @OneToOne(mappedBy = "cartaCredito")
        @Valid
        private Cliente cliente;

        public CartaCredito() {
        }

        public CartaCredito(String numeroCarta, Date dataScadenza, String nomeIntestatario, String cognomeIntestatario, String cvc) {
                this.numeroCarta = numeroCarta;
                this.dataScadenza = dataScadenza;
                this.nomeIntestatario = nomeIntestatario;
                this.cognomeIntestatario = cognomeIntestatario;
                this.cvc = cvc;

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

        public String getCvc() {
                return cvc;
        }

        public void setCvc(String cvc) {
                this.cvc = cvc;
        }

        public Cliente getCliente() {
                return cliente;
        }

        public void setCliente(Cliente cliente) {
                this.cliente = cliente;
        }
}