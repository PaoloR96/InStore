package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="ORDINE")
public class Ordine {
        //definizione variabili
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idOrdine;
        private Date dataProdotto;
        private Float prezzoComplessivo;
        //Relazione unidirezionale tra prodotto e ordine
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "id_ordine")
        private List<Prodotto> listaProdottiOrdine;
        @ManyToOne
        @JoinColumn(name = "cliente_username", nullable = false)
        private Cliente cliente;

        public Ordine() {}

        public Ordine(Long idOrdine, Date dataProdotto, Float prezzoComplessivo, List<Prodotto> listaProdottiOrdine) {
                this.idOrdine = idOrdine;
                this.dataProdotto = dataProdotto;
                this.prezzoComplessivo = prezzoComplessivo;
                this.listaProdottiOrdine = listaProdottiOrdine;
        }

        public Long getIdOrdine() {
                return idOrdine;
        }

        public void setIdOrdine(Long idOrdine) {
                this.idOrdine = idOrdine;
        }

        public Date getDataProdotto() {
                return dataProdotto;
        }

        public void setDataProdotto(Date dataProdotto) {
                this.dataProdotto = dataProdotto;
        }

        public Float getPrezzoComplessivo() {
                return prezzoComplessivo;
        }

        public void setPrezzoComplessivo(Float prezzoComplessivo) {
                this.prezzoComplessivo = prezzoComplessivo;
        }

        public List<Prodotto> getListaProdottiOrdine() {
                return listaProdottiOrdine;
        }

        public void setListaProdottiOrdine(List<Prodotto> listaProdottiOrdine) {
                this.listaProdottiOrdine = listaProdottiOrdine;
        }
}
