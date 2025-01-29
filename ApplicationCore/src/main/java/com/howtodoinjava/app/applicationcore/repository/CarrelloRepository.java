package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, String> {
    Optional<Carrello> findByClienteUsername(String username);
}
