package com.howtodoinjava.app.applicationcore.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class Carrello {
    //definizione variabili
        protected Float prezzoComplessivo;
        protected ArrayList <Prodotto> listaProdottiCarrello;
        //inizializzazione costruttore
        public Carrello() {
            listaProdottiCarrello = new ArrayList();
            prezzoComplessivo = 0.0f;
        }
        //Get&Set
        public Float getPrezzoComplessivo() {
            return prezzoComplessivo;
        }

        public void setPrezzoComplessivo(Float prezzoComplessivo) {
            this.prezzoComplessivo = prezzoComplessivo;
        }

        public ArrayList<Prodotto> getListaProdottiCarrello() {
            return listaProdottiCarrello;
        }

        public void setListaProdottiCarrello(ArrayList<Prodotto> listaProdottiCarrello) {
            this.listaProdottiCarrello = listaProdottiCarrello;
        }
}
