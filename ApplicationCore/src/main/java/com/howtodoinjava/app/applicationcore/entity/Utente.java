package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Utente {
    // Variabili di istanza
    private String email;
    @Id
    private String username;
    private String password;
    private String numCell;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumCell() {
        return numCell;
    }

    public void setNumCell(String numCell) {
        this.numCell = numCell;
    }

    public Utente() {}
    public Utente(String email, String username, String password, String numCell) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.numCell = numCell;
    }
}