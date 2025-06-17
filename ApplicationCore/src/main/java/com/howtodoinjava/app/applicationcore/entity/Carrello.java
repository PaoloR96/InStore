package com.howtodoinjava.app.applicationcore.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARRELLO")
public class Carrello {

        @Id
        @Column(name = "username")
        @NotBlank(message = "L'ID del carrello (username) non può essere vuoto.") // Se l'ID è l'username del cliente
        @Size(min = 3, max = 50, message = "L'ID del carrello deve avere tra 3 e 50 caratteri.")
        private String id;

        @OneToOne(orphanRemoval = true)
        @MapsId
        @JoinColumn(name = "username", nullable = false)
        @NotNull(message = "Il cliente associato al carrello non può essere nullo.")
        @Valid
        @JsonIgnore
        private Cliente cliente;

        @Column(name = "prezzo_complessivo", nullable = false)
        @NotNull(message = "Il prezzo complessivo del carrello non può essere nullo.")
        @DecimalMin(value = "0.0", message = "Il prezzo complessivo del carrello non può essere negativo.")
        private Float prezzoComplessivo;

        @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
        @Valid
        private List<ProdottoCarrello> listaProdottiCarrello;


        public Carrello() {}

        public Carrello(Cliente cliente, Float prezzoComplessivo) {
                this.cliente = cliente;
                this.prezzoComplessivo = prezzoComplessivo;
        }

        public String getId() {
                return id;
        }

        public void setId(String id) {
                this.id = id;
        }

        public Cliente getCliente() {
                return cliente;
        }

        public void setCliente(Cliente cliente) {
                this.cliente = cliente;
        }

        public Float getPrezzoComplessivo() {
                return prezzoComplessivo;
        }

        public void setPrezzoComplessivo(Float prezzoComplessivo) {
                this.prezzoComplessivo = prezzoComplessivo;
        }

        public List<ProdottoCarrello> getListaProdottiCarrello() {
                return listaProdottiCarrello;
        }

        public void setListaProdottiCarrello(List<ProdottoCarrello> listaProdottiCarrello) {
                this.listaProdottiCarrello = listaProdottiCarrello;
        }

        public void escape(){
                this.listaProdottiCarrello.forEach(ProdottoCarrello::escape);
        }
}
