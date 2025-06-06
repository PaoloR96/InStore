package com.howtodoinjava.app.applicationcore.entity;
import jakarta.persistence.*;
import java.util.List;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="CARRELLO")
public class Carrello {

        @Id
        @Column(name = "username")
        private String id;

        @OneToOne(orphanRemoval = true)
        @MapsId
        @JoinColumn(name = "username", nullable = false)
        private Cliente cliente;

        @Column(name = "prezzo_complessivo", nullable = false)
        private Float prezzoComplessivo;

        @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
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
