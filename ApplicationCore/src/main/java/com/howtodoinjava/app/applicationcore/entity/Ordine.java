package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

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
        private Date dataOrdine;

        @Column(name = "prezzo_complessivo", nullable = false)
        private Float prezzoComplessivo;

        @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<ProdottoOrdine> listaProdottiOrdine;

        @ManyToOne
        @JoinColumn(name = "cliente_username", nullable = false)
        private Cliente cliente;

        public Ordine() {}

        public Ordine(Long idOrdine, Date dataOrdine, Float prezzoComplessivo, List<ProdottoOrdine> listaProdottiOrdine, Cliente cliente) {
                this.idOrdine = idOrdine;
                this.dataOrdine = dataOrdine;
                this.prezzoComplessivo = prezzoComplessivo;
                this.listaProdottiOrdine = listaProdottiOrdine;
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
