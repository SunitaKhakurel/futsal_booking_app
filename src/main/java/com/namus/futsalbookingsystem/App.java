package com.namus.futsalbookingsystem;


import com.namus.futsalbookingsystem.config.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {

        try {
           // FirebaseConfig.initializeFirebase();

            SpringApplication.run(App.class, args);
        }catch(Exception e){
            System.out.println(e);
        }

    }
}
