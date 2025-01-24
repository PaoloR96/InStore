package com.howtodoinjava.app.applicationcore.entity;
import jakarta.persistence.*;
import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARRELLO")
public class Carrello {
        //definizione variabili
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;
        private Float prezzoComplessivo;
        @OneToMany(cascade = CascadeType.ALL)
        @JoinColumn(name = "id_carrello")
        private List<Prodotto> listaProdottiCarrello;
        @OneToOne
        private Cliente cliente;

        public Carrello() {}

        public Carrello(Long id, Float prezzoComplessivo, List<Prodotto> listaProdottiCarrello, Cliente cliente) {
                this.id = id;
                this.prezzoComplessivo = prezzoComplessivo;
                this.listaProdottiCarrello = listaProdottiCarrello;
                this.cliente = cliente;
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

        public List<Prodotto> getListaProdottiCarrello() {
                return listaProdottiCarrello;
        }

        public void setListaProdottiCarrello(List<Prodotto> listaProdottiCarrello) {
                this.listaProdottiCarrello = listaProdottiCarrello;
        }
}
