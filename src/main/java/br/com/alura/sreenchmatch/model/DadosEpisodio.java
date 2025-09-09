package br.com.alura.sreenchmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String titulo, @JsonAlias("Episode") Integer numeroEp,
                            @JsonAlias("imdbRating") String avaliacao, @JsonAlias("Released") String dataLancamento) {
}
