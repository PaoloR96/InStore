package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, Integer> {
}
