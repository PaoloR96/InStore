package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrello;
import com.howtodoinjava.app.applicationcore.entity.ProdottoCarrelloId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrdottoCarrelloRepository extends JpaRepository<ProdottoCarrello, ProdottoCarrelloId> {
//    List<ProdottoCarrello> findByIdCarrello(String username);
//    List<ProdottoCarrello> findByIdProdotto(Long idProdotto);
}
