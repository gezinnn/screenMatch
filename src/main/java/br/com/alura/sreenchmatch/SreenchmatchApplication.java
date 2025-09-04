package br.com.alura.sreenchmatch;

import br.com.alura.sreenchmatch.model.DadosSerie;
import br.com.alura.sreenchmatch.service.ConsumoAPI;
import br.com.alura.sreenchmatch.service.ConverteDados;
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
		var consumoAPI = new ConsumoAPI();
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=arrow&apiKey=af9672c7");
		System.out.println(json);

		ConverteDados converteDados = new ConverteDados();
		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);

	}
}
