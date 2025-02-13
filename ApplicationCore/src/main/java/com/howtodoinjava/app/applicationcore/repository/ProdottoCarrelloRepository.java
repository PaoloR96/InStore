package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Carrello;
import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrello;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrelloId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoCarrelloRepository extends JpaRepository<ProdottoCarrello, ProdottoCarrelloId> {

    List<ProdottoCarrello> findByCarrello(Carrello carrello);
    @Query("SELECT p.quantita FROM ProdottoCarrello p WHERE p.prodotto = :prodotto AND p.carrello = :carrello")
    Integer findQuantitaByProdottoAndCarrello(@Param("prodotto") Prodotto prodotto, @Param("carrello") Carrello carrello);
}
