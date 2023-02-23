package io.github.curso.msavaliadorcredito.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartaoCliente {

    private String nome;
    private String bandeira;
    private BigDecimal limiteLiberado;
}
