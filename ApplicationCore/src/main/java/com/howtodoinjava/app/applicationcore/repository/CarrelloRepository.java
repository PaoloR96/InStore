package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CarrelloRepository extends JpaRepository<Carrello, String> {
    Optional<Carrello> findByClienteUsername(String username);


    @Modifying
    @Transactional
    @Query(value = "UPDATE carrello c " +
            "SET c.prezzo_complessivo = (" +
            "  SELECT COALESCE( SUM(pc.quantita * p.prezzo), 0)" +
            "  FROM prodotto_carrello pc " +
            "  JOIN prodotto p ON pc.id_prodotto = p.id_prodotto " +
            "  WHERE pc.username = c.username" +
            ") " +
            "WHERE c.username = :username", nativeQuery = true)
    void updatePrezzoComplessivoByUsername(@Param("username") String username);

    // Metodo per recuperare il prezzo complessivo
    @Query("SELECT c.prezzoComplessivo FROM Carrello c WHERE c.id = :username")
    Float findPrezzoComplessivoByUsername(@Param("username") String username);

}
