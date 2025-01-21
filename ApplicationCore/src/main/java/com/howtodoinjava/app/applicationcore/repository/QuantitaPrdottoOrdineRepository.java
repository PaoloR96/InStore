package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.QuantitaPrdottoOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantitaPrdottoOrdineRepository extends JpaRepository<QuantitaPrdottoOrdine, Integer> {
}
