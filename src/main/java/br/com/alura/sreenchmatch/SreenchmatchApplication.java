package br.com.alura.sreenchmatch;

import br.com.alura.sreenchmatch.main.Main;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SreenchmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SreenchmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Main principal = new Main();
        principal.exibeMenu();


    }
}
