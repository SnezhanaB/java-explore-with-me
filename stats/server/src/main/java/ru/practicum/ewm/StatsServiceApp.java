package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsServiceApp {
    public static void main(String[] args) {
        System.out.println("Hello world from stats-server!");
        SpringApplication.run(StatsServiceApp.class, args);
    }
}