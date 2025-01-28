package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.ProdottoOrdine;
import com.howtodoinjava.app.applicationcore.entity.ProdottoOrdineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoOrdineRepository extends JpaRepository<ProdottoOrdine, ProdottoOrdineId> {
}
