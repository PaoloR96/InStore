package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.QuantitaPrdottoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitaPrdottoCarrelloRepository extends JpaRepository<QuantitaPrdottoCarrello, Integer> {
}
