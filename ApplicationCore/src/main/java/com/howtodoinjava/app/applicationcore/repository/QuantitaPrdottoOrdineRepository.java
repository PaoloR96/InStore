package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.ProdottoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitaPrdottoOrdineRepository extends JpaRepository<ProdottoOrdine, Integer> {
}
