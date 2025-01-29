package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

//    List<Prodotto> findByCarrelloId(Long idCarrello);

//    @Modifying
//    @Query("UPDATE Prodotto p SET p.carrello.idCarrello = :idCarrello WHERE p.idProdotto = :idProdotto")
//    void updateIdCarrello(@Param("idProdotto") Long idProdotto, @Param("idCarrello") Long idCarrello);
}
