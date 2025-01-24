package com.howtodoinjava.app.applicationcore.repository;



import com.howtodoinjava.app.applicationcore.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtenteRepository extends JpaRepository<Utente, String> {
}
