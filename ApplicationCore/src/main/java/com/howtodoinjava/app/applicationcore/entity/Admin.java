package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="ADMIN")
public class Admin extends Utente {

    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;

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

    public Admin() {

    }


    public Admin(String email, String username, String numCell, String nome, String cognome) {
        super(email, username, numCell);
        this.nome = nome;
        this.cognome = cognome;
    }
}