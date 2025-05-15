package com.howtodoinjava.app.applicationcore.dto;

import com.howtodoinjava.app.applicationcore.entity.Utente;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

public class UtenteDTO extends Utente {
    private final List<String> roles;
    private final boolean enabled;

    public UtenteDTO(
            String username,
            String email,
            String numCell,
            List<String> roles,
            boolean enabled
    ){
        super(username, email, numCell);
        this.roles = roles;
        this.enabled = enabled;
    }


    public List<String> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void escape(){
        this.email = HtmlUtils.htmlEscape(this.email);
        this.numCell = HtmlUtils.htmlEscape(this.numCell);
        this.username = HtmlUtils.htmlEscape(this.username);
    }
}
