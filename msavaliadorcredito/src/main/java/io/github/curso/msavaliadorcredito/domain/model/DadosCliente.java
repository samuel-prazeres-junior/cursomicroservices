package io.github.curso.msavaliadorcredito.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosCliente {
    private Long id;
    private String nome;
    private Integer idade;

}
