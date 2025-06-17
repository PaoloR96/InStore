package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="ORDINE")
public class Ordine {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_ordine")
        private Long idOrdine;

        @Column(name = "data_ordine", nullable = false)
        @Temporal(TemporalType.TIMESTAMP)
        @NotNull(message = "La data dell'ordine non può essere nulla.")
        private Date dataOrdine;

        @Column(name = "prezzo_complessivo", nullable = false)
        @NotNull(message = "Il prezzo complessivo dell'ordine non può essere nullo.")
        @DecimalMin(value = "0.0", message = "Il prezzo complessivo non può essere negativo.")
        private Float prezzoComplessivo;

        @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
        @Valid
        private List<ProdottoOrdine> listaProdottiOrdine;

        @ManyToOne
        @NotNull(message = "Il cliente associato all'ordine non può essere nullo.")
        @Valid
        @JoinColumn(name = "cliente_username", nullable = false)
        private Cliente cliente;

        public Ordine() {}

        public Ordine(Date dataOrdine, Float prezzoComplessivo, Cliente cliente) {

                this.dataOrdine = dataOrdine;
                this.prezzoComplessivo = prezzoComplessivo;
                this.cliente = cliente;
        }

        public Long getIdOrdine() {
                return idOrdine;
        }

        public void setIdOrdine(Long idOrdine) {
                this.idOrdine = idOrdine;
        }

        public Date getDataOrdine() {
                return dataOrdine;
        }

        public void setDataOrdine(Date dataOrdine) {
                this.dataOrdine = dataOrdine;
        }

        public Float getPrezzoComplessivo() {
                return prezzoComplessivo;
        }

        public void setPrezzoComplessivo(Float prezzoComplessivo) {
                this.prezzoComplessivo = prezzoComplessivo;
        }

        public List<ProdottoOrdine> getListaProdottiOrdine() {
                return listaProdottiOrdine;
        }

        public void setListaProdottiOrdine(List<ProdottoOrdine> listaProdottiOrdine) {
                this.listaProdottiOrdine = listaProdottiOrdine;
        }

        public Cliente getCliente() {
                return cliente;
        }

        public void setCliente(Cliente cliente) {
                this.cliente = cliente;
        }
}
