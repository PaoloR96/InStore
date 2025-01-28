package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Rivenditore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RivenditoreRepository extends JpaRepository<Rivenditore,String> {
    Optional<Rivenditore> findByUsername(String username);
}
