package com.howtodoinjava.app.applicationcore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@MappedSuperclass
public abstract class Utente {

    @Id
    @Column(name = "username", nullable = false)
    @NotBlank(message = "L'username non può essere vuoto.")
    @Size(min = 3, max = 50, message = "L'username deve avere tra 3 e 50 caratteri.")
    protected String username;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "L'email non può essere vuota.")
    @Email(message = "Il formato dell'email non è valido.")
    @Size(max = 100, message = "L'email non può superare i 100 caratteri.")
    protected String email;

    @Column(name = "num_cell", nullable = false, unique = true)
    @NotBlank(message = "Il numero di cellulare non può essere vuoto.")
    @Pattern(regexp = "^[0-9]{10}$", message = "Il numero di cellulare deve avere 10 cifre numeriche.")
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