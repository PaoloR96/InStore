package com.howtodoinjava.app.applicationcore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;


//TODO CLASSE DTO E MAPPER

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_cliente", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("STANDARD")
@Table(name = "CLIENTE")
public class Cliente extends Utente {

        @Column(nullable = false)
        @NotBlank(message = "Il nome del cliente non può essere vuoto.")
        @Size(max = 50, message = "Il nome del cliente non può superare i 50 caratteri.")
        private String nome;

        @Column(nullable = false)
        @NotBlank(message = "Il cognome del cliente non può essere vuoto.")
        @Size(max = 50, message = "Il cognome del cliente non può superare i 50 caratteri.")
        private String cognome;

        @OneToOne
        @JoinColumn(name = "numero_carta_credito", nullable = false)
        @NotNull(message = "La carta di credito non può essere nulla.")
        @Valid
        @JsonIgnore
        private CartaCredito cartaCredito;

        @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
        @PrimaryKeyJoinColumn
        @NotNull(message = "Il carrello non può essere nullo per il cliente.") // Ogni cliente ha un carrello
        @Valid
        @JsonIgnore
        private Carrello carrello;

        @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
        @Valid
        @JsonIgnore
        private List<Ordine> listaClienteOrdini;

        public Cliente() {}

        public Cliente(String nome,
                       String cognome,
                       CartaCredito cartaCredito,
                       Carrello carrello,
//                       StatoCliente statoCliente,
                       List<Ordine> listaClienteOrdini
        ) {
                this.nome = nome;
                this.cognome = cognome;
                this.cartaCredito = cartaCredito;
                this.carrello = carrello;
//                this.statoCliente = statoCliente;
                this.listaClienteOrdini = listaClienteOrdini;
        }

        public Cliente(String email,
                       String username,
                       String numCell,
//                       boolean status,
                       String nome,
                       String cognome,
                       CartaCredito cartaCredito
//                       StatoCliente statoCliente
        ) {
                super(username, email, numCell);
//                super(username, email, numCell, status);
                this.nome = nome;
                this.cognome = cognome;
                this.cartaCredito = cartaCredito;
//                this.statoCliente = statoCliente;
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

        public List<Ordine> getListaClienteOrdini() {
                return listaClienteOrdini;
        }

        public void setListaClienteOrdini(List<Ordine> listaClienteOrdini) {
                this.listaClienteOrdini = listaClienteOrdini;
        }

}
