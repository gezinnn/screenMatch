package br.com.alura.sreenchmatch.main;

import br.com.alura.sreenchmatch.model.DadosEpisodio;
import br.com.alura.sreenchmatch.model.DadosSerie;
import br.com.alura.sreenchmatch.model.DadosTemporada;
import br.com.alura.sreenchmatch.model.Episodio;
import br.com.alura.sreenchmatch.service.ConsumoAPI;
import br.com.alura.sreenchmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apiKey=" + System.getenv("API_KEY");
    private final String TEMPORADA = "&season=";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();

    public void exibeMenu() {
        System.out.println("Digite o nome da série para buscar: ");
        var nomeSerie = leitura.nextLine().trim(); // normalização da entrada

        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);

        if (dadosSerie == null || dadosSerie.totalTemporadas() == null) {
            System.out.println("❌ Não foi possível encontrar a série: " + nomeSerie);
            return;
        }

        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + TEMPORADA + i + API_KEY);
            DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
            if (dadosTemporada != null) {
                temporadas.add(dadosTemporada);
            }
        }

        // lista todas as temporadas e episódios
        temporadas.forEach(System.out::println);
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        // Top 5 episódios mais bem avaliados
        System.out.println("\n📌 Top 5 - Episódios Mais Bem Avaliados");
        dadosEpisodios.stream()
                .filter(e -> e.avaliacao() != null && !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(
                        e -> Double.valueOf(e.avaliacao()), Comparator.reverseOrder()
                ))
                .limit(5)
                .forEach(this::exibirEpisodioFormatado);
        System.out.println("----------------------------");

        // Converte para objetos de domínio Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        // Filtro por ano de lançamento
        System.out.println("\nA partir de que ano você deseja ver os episódios?");
        int ano;
        try {
            ano = leitura.nextInt();
            leitura.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("❌ Ano inválido. Encerrando busca.");
            return;
        }

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " | Episódio: " + e.getTitulo() +
                                " | Data: " + e.getDataLancamento().format(formatter)
                ));
    }

    private void exibirEpisodioFormatado(DadosEpisodio e) {
        System.out.println("Título: " + e.titulo() + " | Avaliação: " + e.avaliacao());
    }
}
