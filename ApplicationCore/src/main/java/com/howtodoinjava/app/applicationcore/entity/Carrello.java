package com.howtodoinjava.app.applicationcore.entity;
import jakarta.persistence.*;

import java.util.ArrayList;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARRELLO")
public class Carrello {
        //definizione variabili
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private Float prezzoComplessivo;
        @OneToMany
        private ArrayList <Prodotto> listaProdottiCarrello;

        public Carrello() {}

        public Carrello(Long id, Float prezzoComplessivo, ArrayList<Prodotto> listaProdottiCarrello) {
                this.id = id;
                this.prezzoComplessivo = prezzoComplessivo;
                this.listaProdottiCarrello = listaProdottiCarrello;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

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
