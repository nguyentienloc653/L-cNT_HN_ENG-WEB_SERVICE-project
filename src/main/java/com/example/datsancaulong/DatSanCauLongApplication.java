package com.example.datsancaulong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class DatSanCauLongApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatSanCauLongApplication.class, args);
    }

}
