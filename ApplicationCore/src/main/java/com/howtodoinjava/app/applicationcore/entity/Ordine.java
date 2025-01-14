package com.howtodoinjava.app.applicationcore.entity;

import java.util.ArrayList;
import java.util.Date;

public class Ordine {
        //definizione variabili
        protected Long idOrdine;
        protected Date dataProdotto;
        protected Float prezzoComplessivo;
        protected ArrayList <Prodotto> listaProdottiOrdine;
        //costruttore
        public Ordine(Long idOrdine, Date dataProdotto, Float prezzoComplessivo, ArrayList<Prodotto> listaProdottiOrdine) {
            this.idOrdine = idOrdine;
            this.dataProdotto = dataProdotto;
            this.prezzoComplessivo = prezzoComplessivo;
            this.listaProdottiOrdine = listaProdottiOrdine;
        }
        //Get&Set
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

        public ArrayList<Prodotto> getListaProdottiOrdine() {
            return listaProdottiOrdine;
        }

        public void setListaProdottiOrdine(ArrayList<Prodotto> listaProdottiOrdine) {
            this.listaProdottiOrdine = listaProdottiOrdine;
        }
}
