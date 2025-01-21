package com.howtodoinjava.app.applicationcore.repository;

<<<<<<<< HEAD:ApplicationCore/src/main/java/com/howtodoinjava/app/applicationcore/repository/ClienteRepository.java
public class ClienteRepository {
========
import com.howtodoinjava.app.applicationcore.entity.Sconto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScontoRepository extends JpaRepository<Sconto, Integer> {
>>>>>>>> github/master:ApplicationCore/src/main/java/com/howtodoinjava/app/applicationcore/repository/ScontoRepository.java
}
