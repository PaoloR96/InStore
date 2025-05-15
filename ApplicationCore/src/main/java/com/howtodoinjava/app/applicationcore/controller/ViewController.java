package com.howtodoinjava.app.applicationcore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("complete-registration")
    public String completeRegistration(){
        return "complete-registration";
    }

//    Cliente
    @GetMapping("/cliente/index")
    public String clienteIndex() {
        return "cliente/index";
    }

    @GetMapping("/cliente/profile")
    public String clienteProfile() {
        return "cliente/profile";
    }

//    Admin
    @GetMapping("/admin/index")
    public String adminIndex() {
        return "admin/index";
    }

//    Rivenditore
    @GetMapping("/rivenditore/index")
    public String rivenditoreIndex() {
        return "rivenditore/index";
    }

    @GetMapping("/rivenditore/profile")
    public String rivenditoreProfile() {
        return "rivenditore/profile";
    }

}
