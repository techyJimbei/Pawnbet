package com.shrutymalviya.pawnbet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class PawnBetApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(PawnBetApplication.class, args);
    }

}
