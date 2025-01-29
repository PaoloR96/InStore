package com.howtodoinjava.app.applicationcore.repository;

import com.howtodoinjava.app.applicationcore.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String>{

    @Modifying
    @Query(value = "UPDATE cliente SET tipo_cliente = 'PREMIUM', sconto = :sconto WHERE username = :username", nativeQuery = true)
    void upgradeClientePremium(@Param("username") String username, @Param("sconto") Integer sconto);

}
