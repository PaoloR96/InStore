package com.howtodoinjava.app.applicationcore;

import com.howtodoinjava.app.applicationcore.entity.Prodotto;
import com.howtodoinjava.app.applicationcore.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;
@EnableTransactionManagement
@SpringBootApplication
public class ApplicationCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCoreApplication.class, args);}
}
