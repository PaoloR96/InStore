package com.howtodoinjava.app.applicationcore.repository;


import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
}
