package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//TODO CLASSE DTO E MAPPER

@Entity
@Table(name="ADMIN")
public class Admin extends Utente {

    @Column(nullable = false)
    @NotBlank(message = "Il nome dell'admin non può essere vuoto.")
    @Size(max = 50, message = "Il nome dell'admin non può superare i 50 caratteri.")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "Il cognome dell'admin non può essere vuoto.")
    @Size(max = 50, message = "Il cognome dell'admin non può superare i 50 caratteri.")
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