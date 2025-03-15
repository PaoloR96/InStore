package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class Utente {

    @Id
    @Column(name = "username", nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
//    TODO remove this
//    @Column(nullable = false)
//    private String password;
    @Column(name="num_cell",nullable = false, unique = true)
    private String numCell;

    public Utente() {}

//      TODO remove this
//    public Utente(String username, String email, String password, String numCell) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.numCell = numCell;
//    }

    public Utente(String username, String email, String numCell) {
        this.username = username;
        this.email = email;
        this.numCell = numCell;
    }

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


//    TODO remove this
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getNumCell() {
        return numCell;
    }

    public void setNumCell(String numCell) {
        this.numCell = numCell;
    }
}