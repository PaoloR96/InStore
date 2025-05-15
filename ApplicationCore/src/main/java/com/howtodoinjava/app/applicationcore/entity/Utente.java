package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Utente {

    @Id
    @Column(name = "username", nullable = false)
    protected String username;
    @Column(unique = true, nullable = false)
    protected String email;
    @Column(name = "num_cell", nullable = false, unique = true)
    protected String numCell;

    public Utente() {
    }

    public Utente(String username, String email, String numCell) {
        this.username = username;
        this.email = email;
        this.numCell = numCell;
    }

//    public Utente(String username, String email, String numCell, boolean status){
//        this(username, email, numCell);
//        this.enabled = status;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumCell() {
        return numCell;
    }

    public void setNumCell(String numCell) {
        this.numCell = numCell;
    }

}