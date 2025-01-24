package com.howtodoinjava.app.applicationcore.dto;

public class UtenteDTO {
    private String email;
    private String username;
    private String numCell;

    public UtenteDTO() {}

    public UtenteDTO(String email, String username, String numCell) {
        this.email = email;
        this.username = username;
        this.numCell = numCell;
    }

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

    public String getNumCell() {
        return numCell;
    }

    public void setNumCell(String numCell) {
        this.numCell = numCell;
    }
}
